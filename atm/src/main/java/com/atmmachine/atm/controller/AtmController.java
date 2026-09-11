package com.atmmachine.atm.controller;

import com.atmmachine.atm.models.ATM;

/**
 * Thin controller (same idea as VendingController / GameController in Chess).
 * Keeps CLI / API layer free of business rules — ATM + States own the logic.
 */
public class AtmController {

    /** Feature 1: start session by inserting card. */
    public void insertCard(ATM atm, String cardNumber) {
        atm.insertCard(cardNumber);
    }

    /** Feature 1: verify PIN after card insert. */
    public void enterPin(ATM atm, String pin) {
        atm.enterPin(pin);
    }

    /** Feature 2: show account balance (Authenticated state only). */
    public int checkBalance(ATM atm) {
        return atm.checkBalance();
    }

    /** Feature 3: withdraw cash from account + ATM dispenser. */
    public void withdraw(ATM atm, int amount) {
        atm.withdraw(amount);
    }

    /** End session — eject card and return to Idle. */
    public void ejectCard(ATM atm) {
        atm.ejectCard();
    }

    public void displayStatus(ATM atm) {
        atm.displayStatus();
    }

    public void displayHistory(ATM atm) {
        atm.displayHistory();
    }
}
