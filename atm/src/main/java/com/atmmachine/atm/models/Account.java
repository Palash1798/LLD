package com.atmmachine.atm.models;

/**
 * Bank account linked to a debit card.
 * Balance is stored as a plain integer (rupees/paise — pick one unit and stay consistent).
 */
public class Account {

    private final String id;
    private final String holderName;
    private int balance;

    public Account(String id, String holderName, int balance) {
        this.id = id;
        this.holderName = holderName;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public String getHolderName() {
        return holderName;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" + id + ", holder='" + holderName + "', balance=" + balance + "}";
    }
}
