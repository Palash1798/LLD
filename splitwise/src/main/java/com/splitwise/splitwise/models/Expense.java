package com.splitwise.splitwise.models;

import com.splitwise.splitwise.enums.SplitType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Step 2e — Immutable expense record (audit trail).
 *
 * Created after split validation; balances are updated separately in BalanceService.
 */
public class Expense {

    private final String id;
    private final String groupId;
    private final String description;
    private final int amountCents;
    private final User paidBy;
    private final SplitType splitType;
    private final List<ExpenseSplit> splits;

    public Expense(String id,
                   String groupId,
                   String description,
                   int amountCents,
                   User paidBy,
                   SplitType splitType,
                   List<ExpenseSplit> splits) {
        this.id = id;
        this.groupId = groupId;
        this.description = description;
        this.amountCents = amountCents;
        this.paidBy = paidBy;
        this.splitType = splitType;
        this.splits = new ArrayList<>(splits);
    }

    public String getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getDescription() {
        return description;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public List<ExpenseSplit> getSplits() {
        return Collections.unmodifiableList(splits);
    }

    @Override
    public String toString() {
        return "Expense{id='" + id + "', desc='" + description + "', amount=" + amountCents
                + " paise, paidBy=" + paidBy.getName() + ", split=" + splitType + ", splits=" + splits + "}";
    }
}
