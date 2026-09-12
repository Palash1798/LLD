package com.splitwise.splitwise.services;

import com.splitwise.splitwise.models.BalanceSheet;
import com.splitwise.splitwise.models.ExpenseSplit;
import com.splitwise.splitwise.models.Group;
import com.splitwise.splitwise.models.PairwiseBalance;
import com.splitwise.splitwise.models.User;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Step 7 — LEDGER FACADE (heart of balance math).
 *
 * ONLY this class mutates BalanceSheet data.
 * Called after every successful expense creation.
 *
 * Algorithm (for each expense):
 *   1. Payer: totalPaid += amount
 *   2. Each participant: totalOwed += their share
 *   3. For participants != payer: payer gets getBack += share; pairwise debts updated both ways
 */
public class BalanceService {

    // =========================================================================
    // CORE: apply expense to all affected balance sheets
    // =========================================================================

    public void applyExpense(User paidBy, List<ExpenseSplit> splits, int totalCents) {
        BalanceSheet payerSheet = paidBy.getBalanceSheet();

        // Step 7.1 — Record gross payment made by payer
        payerSheet.addTotalPaid(totalCents);

        for (ExpenseSplit split : splits) {
            User participant = split.getUser();
            int shareCents = split.getAmountOwedCents();
            BalanceSheet participantSheet = participant.getBalanceSheet();

            // Step 7.2 — Everyone owes their own share (including payer)
            participantSheet.addTotalOwed(shareCents);

            if (participant.getId().equals(paidBy.getId())) {
                // Payer's own share — no pairwise debt with self
                continue;
            }

            // Step 7.3 — Others owe the payer their share
            payerSheet.addTotalGetBack(shareCents);
            addPairwiseGetBack(payerSheet, participant.getId(), shareCents);

            // Step 7.4 — Participant owes payer
            addPairwiseOwe(participantSheet, paidBy.getId(), shareCents);
        }

        System.out.println("[BalanceService] Applied expense: " + paidBy.getName()
                + " paid " + totalCents + " paise, " + splits.size() + " splits");
    }

    private void addPairwiseOwe(BalanceSheet sheet, String counterpartyId, int cents) {
        PairwiseBalance balance = sheet.getOrCreateBalance(counterpartyId);
        balance.addYouOwe(cents);
    }

    private void addPairwiseGetBack(BalanceSheet sheet, String counterpartyId, int cents) {
        PairwiseBalance balance = sheet.getOrCreateBalance(counterpartyId);
        balance.addYouGetBack(cents);
    }

    // =========================================================================
    // QUERIES + pretty-print for demo
    // =========================================================================

    public BalanceSheet getBalanceSheet(User user) {
        return user.getBalanceSheet();
    }

    /** Net balances for group members only (filters out non-members). */
    public Map<String, Integer> getGroupNetBalances(User viewer, Group group) {
        Map<String, Integer> nets = new LinkedHashMap<>();
        BalanceSheet sheet = viewer.getBalanceSheet();

        for (User member : group.getMembers()) {
            if (member.getId().equals(viewer.getId())) {
                continue;
            }
            PairwiseBalance pairwise = sheet.getBalancesByUser().get(member.getId());
            int net = pairwise == null ? 0 : pairwise.netCents();
            if (net != 0) {
                nets.put(member.getId(), net);
            }
        }
        return nets;
    }

    public void printBalanceSheet(User user, UserService userService) {
        BalanceSheet sheet = user.getBalanceSheet();
        System.out.println("---------------------------------------");
        System.out.println("Balance sheet: " + user.getName() + " (" + user.getId() + ")");
        System.out.println("  Total paid (gross):     " + sheet.getTotalPaidCents() + " paise");
        System.out.println("  Total your share:       " + sheet.getTotalOwedCents() + " paise");
        System.out.println("  Total others owe you:   " + sheet.getTotalGetBackCents() + " paise");
        System.out.println("  Pairwise breakdown:");

        if (sheet.getBalancesByUser().isEmpty()) {
            System.out.println("    (no debts)");
        } else {
            for (Map.Entry<String, PairwiseBalance> entry : sheet.getBalancesByUser().entrySet()) {
                String counterpartyId = entry.getKey();
                PairwiseBalance balance = entry.getValue();
                String counterpartyName = resolveName(userService, counterpartyId);
                int net = balance.netCents();

                if (net > 0) {
                    System.out.println("    " + counterpartyName + " owes YOU " + net + " paise");
                } else if (net < 0) {
                    System.out.println("    YOU owe " + counterpartyName + " " + (-net) + " paise");
                } else {
                    System.out.println("    " + counterpartyName + ": settled (gross owe="
                            + balance.getYouOweCents() + ", getBack=" + balance.getYouGetBackCents() + ")");
                }
            }
        }
        System.out.println("---------------------------------------");
    }

    private String resolveName(UserService userService, String userId) {
        try {
            return userService.getUser(userId).getName();
        } catch (RuntimeException ex) {
            return userId;
        }
    }
}
