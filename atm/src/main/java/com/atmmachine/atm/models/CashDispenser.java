package com.atmmachine.atm.models;

import com.atmmachine.atm.exceptions.InsufficientCashException;

/**
 * Tracks physical cash inside this ATM machine (not the customer's bank balance).
 *
 * Interview point: withdraw must check BOTH account balance AND availableCash here.
 */
public class CashDispenser {

    private int availableCash;

    public CashDispenser(int initialCash) {
        this.availableCash = initialCash;
    }

    /**
     * Step 1: read-only check before debiting the account.
     */
    public boolean canDispense(int amount) {
        return amount > 0 && availableCash >= amount;
    }

    /**
     * Step 2: physically reduce cash in the machine after account debit succeeds.
     */
    public void dispense(int amount) {
        if (!canDispense(amount)) {
            throw new InsufficientCashException(availableCash, amount);
        }
        availableCash -= amount;
        System.out.println("[CashDispenser] Dispensed " + amount
                + ". Remaining in ATM=" + availableCash);
    }

    public int getAvailableCash() {
        return availableCash;
    }
}
