package com.splitwise.splitwise.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Step 2b — Running ledger for one user (materialized view).
 *
 * Updated incrementally on each expense via {@link com.splitwise.splitwise.services.BalanceService}.
 * Alternative design: recompute from all expenses on every read (slower, simpler).
 */
public class BalanceSheet {

    /** Sum of all expenses this user paid (gross). */
    private int totalPaidCents;

    /** Sum of this user's own shares across all expenses. */
    private int totalOwedCents;

    /** Gross amount others owe this user (before netting with what user owes others). */
    private int totalGetBackCents;

    /** Per-counterparty gross balances. Key = other user's id. */
    private final Map<String, PairwiseBalance> balancesByUser = new HashMap<>();

    public int getTotalPaidCents() {
        return totalPaidCents;
    }

    public void addTotalPaid(int cents) {
        this.totalPaidCents += cents;
    }

    public int getTotalOwedCents() {
        return totalOwedCents;
    }

    public void addTotalOwed(int cents) {
        this.totalOwedCents += cents;
    }

    public int getTotalGetBackCents() {
        return totalGetBackCents;
    }

    public void addTotalGetBack(int cents) {
        this.totalGetBackCents += cents;
    }

    public Map<String, PairwiseBalance> getBalancesByUser() {
        return balancesByUser;
    }

    public PairwiseBalance getOrCreateBalance(String counterpartyUserId) {
        return balancesByUser.computeIfAbsent(counterpartyUserId, id -> new PairwiseBalance());
    }
}
