package com.splitwise.splitwise.enums;

/**
 * Step 1 — How an expense is divided among participants.
 *
 * Each value maps to a {@link com.splitwise.splitwise.strategies.SplitStrategy} implementation
 * via {@link com.splitwise.splitwise.factories.SplitStrategyFactory}.
 *
 * Interview tip: adding SHARES or BY_ITEMS = new enum + new Strategy class (Open/Closed).
 */
public enum SplitType {
    /** Total divided equally; remainder paise go to first N users. */
    EQUAL,

    /** Caller supplies exact amount per participant; sum must equal total. */
    UNEQUAL,

    /** Caller supplies percentages per participant; must sum to 100. */
    PERCENTAGE
}
