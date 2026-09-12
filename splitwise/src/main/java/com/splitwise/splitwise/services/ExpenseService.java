package com.splitwise.splitwise.services;

import com.splitwise.splitwise.dto.AddExpenseRequest;
import com.splitwise.splitwise.exceptions.InvalidSplitException;
import com.splitwise.splitwise.exceptions.MemberNotInGroupException;
import com.splitwise.splitwise.exceptions.UserNotFoundException;
import com.splitwise.splitwise.factories.SplitStrategyFactory;
import com.splitwise.splitwise.models.Expense;
import com.splitwise.splitwise.models.ExpenseSplit;
import com.splitwise.splitwise.models.Group;
import com.splitwise.splitwise.models.User;
import com.splitwise.splitwise.strategies.SplitStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Step 8 — ORCHESTRATOR (heart of Splitwise LLD).
 *
 * Flow for addExpense:
 *   1. Load group + payer + participants
 *   2. Validate membership + payer in participants
 *   3. SplitStrategy.validate + computeShares
 *   4. Create Expense record on group
 *   5. BalanceService.applyExpense — ONLY after validation passes
 *
 * Does NOT contain if-else per split type — delegates to SplitStrategyFactory.
 */
public class ExpenseService {

    private final GroupService groupService;
    private final UserService userService;
    private final BalanceService balanceService;
    private final SplitStrategyFactory splitStrategyFactory;

    public ExpenseService(GroupService groupService,
                          UserService userService,
                          BalanceService balanceService,
                          SplitStrategyFactory splitStrategyFactory) {
        this.groupService = groupService;
        this.userService = userService;
        this.balanceService = balanceService;
        this.splitStrategyFactory = splitStrategyFactory;
    }

    public Expense addExpense(AddExpenseRequest request) {
        // Step 8.1 — Load group
        Group group = groupService.getGroup(request.getGroupId());

        // Step 8.2 — Load payer
        User paidBy = userService.getUser(request.getPaidByUserId());

        // Step 8.3 — Basic amount check
        if (request.getAmountCents() <= 0) {
            throw new InvalidSplitException("Expense amount must be positive");
        }

        // Step 8.4 — Resolve participants (must be group members)
        List<User> participants = resolveParticipants(group, request.getParticipantUserIds());

        // Step 8.5 — Payer must be in the split list
        boolean payerInSplit = participants.stream().anyMatch(u -> u.getId().equals(paidBy.getId()));
        if (!payerInSplit) {
            throw new InvalidSplitException("Payer must be included in expense participants");
        }

        // Step 8.6 — Strategy validates then computes shares
        SplitStrategy strategy = splitStrategyFactory.getStrategy(request.getSplitType());
        strategy.validate(request.getAmountCents(), participants, request.getInputValues());
        List<ExpenseSplit> splits = strategy.computeShares(
                request.getAmountCents(),
                participants,
                request.getInputValues()
        );

        // Step 8.7 — Sanity: shares must sum to total (defensive; strategies should guarantee this)
        int splitSum = splits.stream().mapToInt(ExpenseSplit::getAmountOwedCents).sum();
        if (splitSum != request.getAmountCents()) {
            throw new InvalidSplitException("Internal error: splits sum to " + splitSum
                    + " but total is " + request.getAmountCents());
        }

        // Step 8.8 — Persist expense on group (audit trail)
        String expenseId = "EXP-" + UUID.randomUUID().toString().substring(0, 8);
        Expense expense = new Expense(
                expenseId,
                group.getId(),
                request.getDescription(),
                request.getAmountCents(),
                paidBy,
                request.getSplitType(),
                splits
        );
        group.addExpense(expense);

        // Step 8.9 — Update balances (ledger mutation — last step)
        balanceService.applyExpense(paidBy, splits, request.getAmountCents());

        System.out.println("[ExpenseService] Created " + expense);
        return expense;
    }

    private List<User> resolveParticipants(Group group, List<String> participantUserIds) {
        if (participantUserIds == null || participantUserIds.isEmpty()) {
            throw new InvalidSplitException("At least one participant is required");
        }

        List<User> participants = new ArrayList<>();
        for (String userId : participantUserIds) {
            if (!group.isMember(userId)) {
                throw new MemberNotInGroupException(userId, group.getId());
            }
            try {
                participants.add(userService.getUser(userId));
            } catch (UserNotFoundException ex) {
                throw new InvalidSplitException("Unknown participant: " + userId);
            }
        }
        return participants;
    }
}
