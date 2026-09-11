package com.atmmachine.atm.factories;

import com.atmmachine.atm.models.Account;
import com.atmmachine.atm.models.Card;
import com.atmmachine.atm.services.BankingService;

/**
 * Seeds demo accounts + cards so CLI walkthrough starts with real data.
 *
 * Study tip: memorize one card/PIN pair for quick demos in interviews.
 */
public final class AccountFactory {

    private AccountFactory() {
        // utility class
    }

    /**
     * Default bank data for study demos.
     *
     * Card numbers / PINs (for quick reference):
     *   4111111111111111 / 1234  → Alice, balance 10000
     *   4222222222222222 / 5678  → Bob,   balance 500
     *   4333333333333333 / 9999  → Carol, balance 200000
     */
    public static BankingService createBankingService() {
        BankingService bank = new BankingService();

        // Step 1: create accounts
        Account alice = new Account("ACC001", "Alice", 10_000);
        Account bob = new Account("ACC002", "Bob", 500);
        Account carol = new Account("ACC003", "Carol", 200_000);

        bank.registerAccount(alice);
        bank.registerAccount(bob);
        bank.registerAccount(carol);

        // Step 2: link cards to accounts
        bank.registerCard(new Card("4111111111111111", "ACC001", "1234"));
        bank.registerCard(new Card("4222222222222222", "ACC002", "5678"));
        bank.registerCard(new Card("4333333333333333", "ACC003", "9999"));

        return bank;
    }
}
