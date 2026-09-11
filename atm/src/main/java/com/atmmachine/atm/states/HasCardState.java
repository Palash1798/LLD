package com.atmmachine.atm.states;

import com.atmmachine.atm.enums.AtmSessionStatus;
import com.atmmachine.atm.exceptions.InvalidOperationException;
import com.atmmachine.atm.exceptions.InvalidPinException;
import com.atmmachine.atm.models.ATM;
import com.atmmachine.atm.models.Account;

/**
 * HAS_CARD = card inserted, waiting for PIN verification.
 *
 * Allowed: enterPin (correct → Authenticated; wrong → retry up to 3 times)
 * Allowed: ejectCard → reset to Idle
 * Rejected: insertCard (already inserted), checkBalance, withdraw
 */
public class HasCardState implements AtmState {

    /** Standard ATM rule — block session after this many wrong PINs. */
    public static final int MAX_PIN_ATTEMPTS = 3;

    @Override
    public void insertCard(ATM atm, String cardNumber) {
        throw new InvalidOperationException("Card already inserted. Enter PIN or eject card.");
    }

    @Override
    public void enterPin(ATM atm, String pin) {
        // Step 1: ask the bank to validate PIN
        if (atm.getBankingService().validatePin(atm.getCurrentCard(), pin)) {
            // Step 2: load linked account onto session
            Account account = atm.getBankingService().getAccount(atm.getCurrentCard().getAccountId());
            atm.setCurrentAccount(account);
            atm.setState(new AuthenticatedState());
            System.out.println("[HasCard] PIN accepted. Welcome, " + account.getHolderName() + "!");
            System.out.println("[HasCard] State → Authenticated");
            return;
        }

        // Step 3: wrong PIN — increment attempts
        atm.incrementPinAttempts();
        int attempts = atm.getPinAttempts();

        if (attempts >= MAX_PIN_ATTEMPTS) {
            // Step 4a: too many failures — end session
            System.out.println("[HasCard] Too many wrong PINs. Card retained/ejected. Session ended.");
            atm.resetSession();
            System.out.println("[HasCard] State → Idle");
            return;
        }

        // Step 4b: still have retries left
        int attemptsLeft = MAX_PIN_ATTEMPTS - attempts;
        throw new InvalidPinException(attemptsLeft);
    }

    @Override
    public int checkBalance(ATM atm) {
        throw new InvalidOperationException("Enter PIN before checking balance.");
    }

    @Override
    public void withdraw(ATM atm, int amount) {
        throw new InvalidOperationException("Enter PIN before withdrawing.");
    }

    @Override
    public void ejectCard(ATM atm) {
        System.out.println("[HasCard] Card ejected before authentication.");
        atm.resetSession();
        System.out.println("[HasCard] State → Idle");
    }

    @Override
    public AtmSessionStatus getStatus() {
        return AtmSessionStatus.HAS_CARD;
    }
}
