package com.splitwise.splitwise.strategies;

import com.splitwise.splitwise.exceptions.InvalidSplitException;
import com.splitwise.splitwise.models.ExpenseSplit;
import com.splitwise.splitwise.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Step 4e — EXTENSION: split by percentage (must sum to 100).
 *
 * Rounding: compute floor for all but last participant; last gets remainder
 * so total always matches exactly.
 */
public class PercentageSplitStrategy implements SplitStrategy {

    @Override
    public void validate(int totalCents, List<User> participants, List<Integer> inputValues) {
        if (participants == null || participants.isEmpty()) {
            throw new InvalidSplitException("PERCENTAGE split requires participants");
        }
        if (inputValues == null || inputValues.size() != participants.size()) {
            throw new InvalidSplitException("PERCENTAGE split needs one percentage per participant");
        }
        int sumPct = 0;
        for (int pct : inputValues) {
            if (pct < 0) {
                throw new InvalidSplitException("Percentages cannot be negative");
            }
            sumPct += pct;
        }
        if (sumPct != 100) {
            throw new InvalidSplitException("Percentages must sum to 100, got " + sumPct);
        }
    }

    @Override
    public List<ExpenseSplit> computeShares(int totalCents, List<User> participants, List<Integer> inputValues) {
        List<ExpenseSplit> splits = new ArrayList<>(participants.size());
        int assigned = 0;

        for (int i = 0; i < participants.size(); i++) {
            int share;
            if (i == participants.size() - 1) {
                // Last user absorbs rounding remainder
                share = totalCents - assigned;
            } else {
                share = (totalCents * inputValues.get(i)) / 100;
                assigned += share;
            }
            splits.add(new ExpenseSplit(participants.get(i), share));
        }
        return splits;
    }
}
