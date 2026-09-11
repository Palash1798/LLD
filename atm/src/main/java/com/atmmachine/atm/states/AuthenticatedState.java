package com.atmmachine.atm.states;

import com.atmmachine.atm.enums.AtmSessionStatus;
import com.atmmachine.atm.exceptions.InsufficientBalanceException;
import com.atmmachine.atm.exceptions.InsufficientCashException;
import com.atmmachine.atm.exceptions.InvalidOperationException;
import com.atmmachine.atm.models.ATM;

/**
 * AUTHENTICATED = PIN verified. User can check balance, withdraw, or eject card.
 *
 * On valid withdraw we:
 *   1) validate amount against account balance AND ATM cash
 *   2) move to DispensingState
 *   3) auto-call withdraw() again (same pattern as Vending HasMoney → Dispense)
 */
public class AuthenticatedState implements AtmState {

    @Override
    public void insertCard(ATM atm, String cardNumber) {
        throw new InvalidOperationException("Card already in session. Eject card first.");
    }

    @Override
    public void enterPin(ATM atm, String pin) {
        throw new InvalidOperationException("Already authenticated.");
    }

    @Override
    public int checkBalance(ATM atm) {
        // Step 1: read-only call to bank — no state change
        int balance = atm.getBankingService().getBalance(atm.getCurrentAccount().getId());
        System.out.println("[Authenticated] Balance for " + atm.getCurrentAccount().getHolderName()
                + " = " + balance);
        return balance;
    }

    @Override
    public void withdraw(ATM atm, int amount) {
        // Step 1: basic amount validation
        if (amount <= 0) {
            throw new InvalidOperationException("Withdraw amount must be positive. Got: " + amount);
        }

        String accountId = atm.getCurrentAccount().getId();
        int accountBalance = atm.getBankingService().getBalance(accountId);

        // Step 2: check customer's bank balance
        if (accountBalance < amount) {
            throw new InsufficientBalanceException(accountBalance, amount);
        }

        // Step 3: check physical cash in this ATM (second balance!)
        if (!atm.getCashDispenser().canDispense(amount)) {
            throw new InsufficientCashException(atm.getCashDispenser().getAvailableCash(), amount);
        }

        System.out.println("[Authenticated] Withdraw " + amount + " approved. Moving to dispense...");

        // Step 4: transition Authenticated → Dispensing
        atm.setState(new DispensingState());
        System.out.println("[Authenticated] State → Dispensing");

        // Step 5: auto-dispense (interview simplification — like Vending auto-dispense)
        atm.withdraw(amount);
    }

    @Override
    public void ejectCard(ATM atm) {
        System.out.println("[Authenticated] Thank you. Card ejected.");
        atm.resetSession();
        System.out.println("[Authenticated] State → Idle");
    }

    @Override
    public AtmSessionStatus getStatus() {
        return AtmSessionStatus.AUTHENTICATED;
    }
}
