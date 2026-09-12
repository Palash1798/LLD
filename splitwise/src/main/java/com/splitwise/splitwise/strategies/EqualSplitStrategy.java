package com.splitwise.splitwise.strategies;

import com.splitwise.splitwise.exceptions.InvalidSplitException;
import com.splitwise.splitwise.models.ExpenseSplit;
import com.splitwise.splitwise.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Step 4c — Split total equally among all participants.
 *
 * Rounding: integer division with remainder distributed one paise at a time
 * to the first N users so shares always sum exactly to totalCents.
 *
 * Example: 1000 paise / 3 people → 334 + 333 + 333
 */
public class EqualSplitStrategy implements SplitStrategy {

    @Override
    public void validate(int totalCents, List<User> participants, List<Integer> inputValues) {
        if (participants == null || participants.isEmpty()) {
            throw new InvalidSplitException("EQUAL split requires at least one participant");
        }
        if (totalCents <= 0) {
            throw new InvalidSplitException("Expense amount must be positive");
        }
    }

    @Override
    public List<ExpenseSplit> computeShares(int totalCents, List<User> participants, List<Integer> inputValues) {
        int count = participants.size();
        int baseShare = totalCents / count;
        int remainder = totalCents % count;

        List<ExpenseSplit> splits = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            // First `remainder` users get one extra paise
            int share = baseShare + (i < remainder ? 1 : 0);
            splits.add(new ExpenseSplit(participants.get(i), share));
        }
        return splits;
    }
}
