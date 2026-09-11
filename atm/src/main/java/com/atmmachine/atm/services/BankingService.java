package com.atmmachine.atm.services;

import com.atmmachine.atm.exceptions.InsufficientBalanceException;
import com.atmmachine.atm.models.Account;
import com.atmmachine.atm.models.Card;

import java.util.HashMap;
import java.util.Map;

/**
 * FACADE over the bank's core systems.
 *
 * In a real ATM this would call remote APIs; here we use in-memory maps
 * so you can focus on the State pattern in the ATM class.
 */
public class BankingService {

    private final Map<String, Card> cards = new HashMap<>();
    private final Map<String, Account> accounts = new HashMap<>();

    // -------------------------------------------------------------------------
    // Setup helpers (used by AccountFactory)
    // -------------------------------------------------------------------------

    public void registerAccount(Account account) {
        accounts.put(account.getId(), account);
    }

    public void registerCard(Card card) {
        cards.put(card.getCardNumber(), card);
    }

    // -------------------------------------------------------------------------
    // Read operations
    // -------------------------------------------------------------------------

    public Card getCard(String cardNumber) {
        return cards.get(cardNumber);
    }

    public Account getAccount(String accountId) {
        Account account = accounts.get(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Unknown account: " + accountId);
        }
        return account;
    }

    public boolean validatePin(Card card, String pin) {
        return card.getPin().equals(pin);
    }

    public int getBalance(String accountId) {
        return getAccount(accountId).getBalance();
    }

    // -------------------------------------------------------------------------
    // Write operations
    // -------------------------------------------------------------------------

    /**
     * Step 1 of withdraw: reduce account balance at the bank.
     */
    public void debit(String accountId, int amount) {
        Account account = getAccount(accountId);
        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException(account.getBalance(), amount);
        }
        account.setBalance(account.getBalance() - amount);
        System.out.println("[Bank] Debited " + amount + " from " + accountId
                + ". New balance=" + account.getBalance());
    }

    /**
     * Rollback helper if cash dispense fails after debit.
     */
    public void credit(String accountId, int amount) {
        Account account = getAccount(accountId);
        account.setBalance(account.getBalance() + amount);
        System.out.println("[Bank] Credited " + amount + " back to " + accountId
                + ". New balance=" + account.getBalance());
    }
}
