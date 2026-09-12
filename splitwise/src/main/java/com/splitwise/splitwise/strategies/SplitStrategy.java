package com.splitwise.splitwise.strategies;

import com.splitwise.splitwise.models.ExpenseSplit;
import com.splitwise.splitwise.models.User;

import java.util.List;

/**
 * Step 4 — STRATEGY PATTERN (core of Splitwise LLD).
 *
 * Each split type (Equal, Unequal, Percentage) implements validate + computeShares.
 * ExpenseService never uses if-else on split type — it asks SplitStrategyFactory.
 *
 * Same idea as Payment Gateway {@code PaymentStrategy} — Open/Closed principle.
 */
public interface SplitStrategy {

    /**
     * Step 4a — Reject invalid input before any balance mutation.
     *
     * @param totalCents    full expense amount in paise
     * @param participants  users sharing the expense (payer must be included)
     * @param inputValues   unequal amounts OR percentages per participant (order matches participants);
     *                      may be null/empty for EQUAL
     */
    void validate(int totalCents, List<User> participants, List<Integer> inputValues);

    /**
     * Step 4b — Produce one ExpenseSplit per participant; sum must equal totalCents.
     */
    List<ExpenseSplit> computeShares(int totalCents, List<User> participants, List<Integer> inputValues);
}
