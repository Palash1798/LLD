package com.atmmachine.atm.states;

import com.atmmachine.atm.enums.AtmSessionStatus;
import com.atmmachine.atm.enums.TransactionType;
import com.atmmachine.atm.exceptions.InvalidOperationException;
import com.atmmachine.atm.models.ATM;
import com.atmmachine.atm.models.Transaction;

/**
 * DISPENSING = withdraw validated; now debit account and give cash.
 *
 * Invariant: only enter this state when amount > 0, account has funds, ATM has cash.
 *
 * Rollback: if dispense fails after debit, credit the account back (compensating transaction).
 */
public class DispensingState implements AtmState {

    @Override
    public void insertCard(ATM atm, String cardNumber) {
        throw new InvalidOperationException("Transaction in progress. Please wait.");
    }

    @Override
    public void enterPin(ATM atm, String pin) {
        throw new InvalidOperationException("Transaction in progress. Please wait.");
    }

    @Override
    public int checkBalance(ATM atm) {
        throw new InvalidOperationException("Transaction in progress. Please wait.");
    }

    @Override
    public void withdraw(ATM atm, int amount) {
        String accountId = atm.getCurrentAccount().getId();
        boolean debited = false;

        try {
            // Step 1: debit account at the bank
            atm.getBankingService().debit(accountId, amount);
            debited = true;

            // Step 2: dispense physical cash from the machine
            atm.getCashDispenser().dispense(amount);
            System.out.println("[Dispense] Please collect your cash: " + amount);

            // Step 3: record successful transaction (optional history)
            atm.addTransaction(new Transaction(TransactionType.WITHDRAW, amount, accountId, "SUCCESS"));

            // Step 4: back to Authenticated so user can do more transactions
            atm.setState(new AuthenticatedState());
            System.out.println("[Dispense] State → Authenticated (card still in machine)");

        } catch (RuntimeException ex) {
            // Step 5: rollback if debit succeeded but dispense failed
            if (debited) {
                atm.getBankingService().credit(accountId, amount);
                atm.addTransaction(new Transaction(TransactionType.WITHDRAW, amount, accountId, "FAILED_ROLLBACK"));
            }
            atm.setState(new AuthenticatedState());
            System.out.println("[Dispense] Withdraw failed. State → Authenticated");
            throw ex;
        }
    }

    @Override
    public void ejectCard(ATM atm) {
        throw new InvalidOperationException("Cannot eject card while dispensing cash.");
    }

    @Override
    public AtmSessionStatus getStatus() {
        return AtmSessionStatus.DISPENSING;
    }
}
