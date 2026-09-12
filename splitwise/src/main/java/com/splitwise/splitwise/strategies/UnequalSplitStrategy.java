package com.splitwise.splitwise.strategies;

import com.splitwise.splitwise.exceptions.InvalidSplitException;
import com.splitwise.splitwise.models.ExpenseSplit;
import com.splitwise.splitwise.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Step 4d — Caller supplies exact share per participant (EXACT amounts in Splitwise app).
 *
 * Validation: inputValues.size() == participants.size() AND sum(inputValues) == totalCents
 */
public class UnequalSplitStrategy implements SplitStrategy {

    @Override
    public void validate(int totalCents, List<User> participants, List<Integer> inputValues) {
        if (participants == null || participants.isEmpty()) {
            throw new InvalidSplitException("UNEQUAL split requires participants");
        }
        if (inputValues == null || inputValues.size() != participants.size()) {
            throw new InvalidSplitException("UNEQUAL split needs one amount per participant");
        }
        int sum = 0;
        for (int value : inputValues) {
            if (value < 0) {
                throw new InvalidSplitException("Share amounts cannot be negative");
            }
            sum += value;
        }
        if (sum != totalCents) {
            throw new InvalidSplitException(
                    "UNEQUAL shares sum to " + sum + " but expense total is " + totalCents);
        }
    }

    @Override
    public List<ExpenseSplit> computeShares(int totalCents, List<User> participants, List<Integer> inputValues) {
        List<ExpenseSplit> splits = new ArrayList<>(participants.size());
        for (int i = 0; i < participants.size(); i++) {
            splits.add(new ExpenseSplit(participants.get(i), inputValues.get(i)));
        }
        return splits;
    }
}
