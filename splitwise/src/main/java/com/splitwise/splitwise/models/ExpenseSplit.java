package com.splitwise.splitwise.models;

/**
 * Step 2d — One participant's share of an expense.
 *
 * Immutable record of "user X owed Y paise for this expense" after split calculation.
 */
public class ExpenseSplit {

    private final User user;
    private final int amountOwedCents;

    public ExpenseSplit(User user, int amountOwedCents) {
        this.user = user;
        this.amountOwedCents = amountOwedCents;
    }

    public User getUser() {
        return user;
    }

    public int getAmountOwedCents() {
        return amountOwedCents;
    }

    @Override
    public String toString() {
        return user.getName() + " owes " + amountOwedCents + " paise";
    }
}
