package com.splitwise.splitwise.models;

/**
 * Step 2c — A Splitwise user.
 *
 * Each user owns a {@link BalanceSheet} for O(1) balance lookups.
 * In production, balances might live in a separate table; colocated here for interview simplicity.
 */
public class User {

    private final String id;
    private final String name;
    private final BalanceSheet balanceSheet = new BalanceSheet();

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BalanceSheet getBalanceSheet() {
        return balanceSheet;
    }

    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name + "'}";
    }
}
