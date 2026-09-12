package com.splitwise.splitwise.dto;

import com.splitwise.splitwise.enums.SplitType;

import java.util.List;

/**
 * Step 5b — Input DTO for adding an expense.
 *
 * inputValues meaning depends on splitType:
 *   EQUAL      — ignored (computed automatically)
 *   UNEQUAL    — exact paise per participant (same order as participantUserIds)
 *   PERCENTAGE — integer percentages summing to 100
 */
public class AddExpenseRequest {

    private final String groupId;
    private final String paidByUserId;
    private final int amountCents;
    private final String description;
    private final SplitType splitType;
    private final List<String> participantUserIds;
    private final List<Integer> inputValues;

    public AddExpenseRequest(String groupId,
                             String paidByUserId,
                             int amountCents,
                             String description,
                             SplitType splitType,
                             List<String> participantUserIds,
                             List<Integer> inputValues) {
        this.groupId = groupId;
        this.paidByUserId = paidByUserId;
        this.amountCents = amountCents;
        this.description = description;
        this.splitType = splitType;
        this.participantUserIds = participantUserIds;
        this.inputValues = inputValues;
    }

    /** Convenience for EQUAL split — no input values needed. */
    public static AddExpenseRequest equal(String groupId,
                                          String paidByUserId,
                                          int amountCents,
                                          String description,
                                          List<String> participantUserIds) {
        return new AddExpenseRequest(groupId, paidByUserId, amountCents, description,
                SplitType.EQUAL, participantUserIds, null);
    }

    /** Convenience for UNEQUAL split. */
    public static AddExpenseRequest unequal(String groupId,
                                            String paidByUserId,
                                            int amountCents,
                                            String description,
                                            List<String> participantUserIds,
                                            List<Integer> exactAmountsCents) {
        return new AddExpenseRequest(groupId, paidByUserId, amountCents, description,
                SplitType.UNEQUAL, participantUserIds, exactAmountsCents);
    }

    /** Convenience for PERCENTAGE split. */
    public static AddExpenseRequest percentage(String groupId,
                                               String paidByUserId,
                                               int amountCents,
                                               String description,
                                               List<String> participantUserIds,
                                               List<Integer> percentages) {
        return new AddExpenseRequest(groupId, paidByUserId, amountCents, description,
                SplitType.PERCENTAGE, participantUserIds, percentages);
    }

    public String getGroupId() {
        return groupId;
    }

    public String getPaidByUserId() {
        return paidByUserId;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public String getDescription() {
        return description;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public List<String> getParticipantUserIds() {
        return participantUserIds;
    }

    public List<Integer> getInputValues() {
        return inputValues;
    }
}
