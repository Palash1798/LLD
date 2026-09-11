package com.atmmachine.atm.models;

import com.atmmachine.atm.enums.AtmSessionStatus;
import com.atmmachine.atm.services.BankingService;
import com.atmmachine.atm.states.AtmState;
import com.atmmachine.atm.states.IdleState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CONTEXT in the State pattern.
 *
 * Holds shared session data (card, account, pin attempts, history)
 * and delegates every user action to currentState.
 *
 * Interview one-liner:
 * "ATM never switches on status enum — it asks the current state object what to do."
 */
public class ATM {

    private final BankingService bankingService;
    private final CashDispenser cashDispenser;

    private AtmState currentState;
    private Card currentCard;
    private Account currentAccount;
    private int pinAttempts;
    private final List<Transaction> history = new ArrayList<>();

    public ATM(BankingService bankingService, CashDispenser cashDispenser) {
        this.bankingService = bankingService;
        this.cashDispenser = cashDispenser;
        this.currentState = new IdleState();
        this.pinAttempts = 0;
    }

    // -------------------------------------------------------------------------
    // Public API — always delegate to current state
    // -------------------------------------------------------------------------

    /** Feature 1: insert debit card to start session. */
    public void insertCard(String cardNumber) {
        currentState.insertCard(this, cardNumber);
    }

    /** Feature 1: verify PIN (max 3 attempts in HasCardState). */
    public void enterPin(String pin) {
        currentState.enterPin(this, pin);
    }

    /** Feature 2: read account balance (Authenticated only). */
    public int checkBalance() {
        return currentState.checkBalance(this);
    }

    /** Feature 3: withdraw cash (Authenticated → Dispensing → Authenticated). */
    public void withdraw(int amount) {
        currentState.withdraw(this, amount);
    }

    /** End session and return card. */
    public void ejectCard() {
        currentState.ejectCard(this);
    }

    // -------------------------------------------------------------------------
    // Helpers used BY states (context API)
    // -------------------------------------------------------------------------

    public void setState(AtmState state) {
        this.currentState = state;
    }

    public AtmState getCurrentState() {
        return currentState;
    }

    public AtmSessionStatus getStatus() {
        return currentState.getStatus();
    }

    public BankingService getBankingService() {
        return bankingService;
    }

    public CashDispenser getCashDispenser() {
        return cashDispenser;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    public int getPinAttempts() {
        return pinAttempts;
    }

    public void setPinAttempts(int pinAttempts) {
        this.pinAttempts = pinAttempts;
    }

    public void incrementPinAttempts() {
        this.pinAttempts++;
    }

    public void addTransaction(Transaction transaction) {
        history.add(transaction);
    }

    public List<Transaction> getHistory() {
        return Collections.unmodifiableList(history);
    }

    /**
     * Clear session and return to Idle (after eject or PIN lockout).
     */
    public void resetSession() {
        this.currentCard = null;
        this.currentAccount = null;
        this.pinAttempts = 0;
        this.currentState = new IdleState();
    }

    public void displayStatus() {
        System.out.println("Status=" + getStatus()
                + ", ATM cash=" + cashDispenser.getAvailableCash()
                + ", card=" + (currentCard != null ? currentCard : "none")
                + ", account=" + (currentAccount != null ? currentAccount.getHolderName() : "none"));
    }

    public void displayHistory() {
        System.out.println("---------- TRANSACTIONS ----------");
        if (history.isEmpty()) {
            System.out.println("(none yet)");
        } else {
            for (Transaction txn : history) {
                System.out.println(txn);
            }
        }
        System.out.println("----------------------------------");
    }
}
