package com.atmmachine.atm.states;

import com.atmmachine.atm.enums.AtmSessionStatus;
import com.atmmachine.atm.exceptions.InvalidCardException;
import com.atmmachine.atm.exceptions.InvalidOperationException;
import com.atmmachine.atm.models.ATM;
import com.atmmachine.atm.models.Card;

/**
 * IDLE = no card in machine. Waiting for customer to insert card.
 *
 * Allowed: insertCard → go to HasCard
 * Rejected: enterPin, checkBalance, withdraw
 * Eject: no-op
 */
public class IdleState implements AtmState {

    @Override
    public void insertCard(ATM atm, String cardNumber) {
        // Step 1: look up card at the bank
        Card card = atm.getBankingService().getCard(cardNumber);
        if (card == null) {
            throw new InvalidCardException(cardNumber);
        }

        // Step 2: start a new session on the context
        atm.setCurrentCard(card);
        atm.setPinAttempts(0);
        System.out.println("[Idle] Card inserted: " + card);

        // Step 3: transition Idle → HasCard
        atm.setState(new HasCardState());
        System.out.println("[Idle] State → HasCard. Please enter PIN.");
    }

    @Override
    public void enterPin(ATM atm, String pin) {
        throw new InvalidOperationException("Insert a card before entering PIN.");
    }

    @Override
    public int checkBalance(ATM atm) {
        throw new InvalidOperationException("Insert a card and enter PIN to check balance.");
    }

    @Override
    public void withdraw(ATM atm, int amount) {
        throw new InvalidOperationException("Insert a card and enter PIN before withdrawing.");
    }

    @Override
    public void ejectCard(ATM atm) {
        System.out.println("[Idle] No card to eject.");
    }

    @Override
    public AtmSessionStatus getStatus() {
        return AtmSessionStatus.IDLE;
    }
}
