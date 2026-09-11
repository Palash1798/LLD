package com.atmmachine.atm;

import com.atmmachine.atm.controller.AtmController;
import com.atmmachine.atm.factories.AccountFactory;
import com.atmmachine.atm.models.ATM;
import com.atmmachine.atm.models.CashDispenser;
import com.atmmachine.atm.services.BankingService;

/**
 * Entry point of the application.
 *
 * Runs a scripted study demo so you can practice the State-pattern
 * flow without needing the Spring web stack.
 *
 * Walks through the 3 MVP features from LLD_ATM_MACHINE.md:
 *   1) Insert card + PIN authentication
 *   2) Balance inquiry
 *   3) Withdraw cash + eject card
 *
 * IDE: run this class (or AtmDemo for interactive menu).
 */
public class AtmApplication {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  ATM MACHINE LLD — STUDY DEMO");
        System.out.println("  Pattern: State (Idle/HasCard/Auth/Dispense)");
        System.out.println("========================================\n");

        // Step 0: wire objects (like interview setup)
        BankingService bank = AccountFactory.createBankingService();
        CashDispenser dispenser = new CashDispenser(50_000); // ₹50,000 cash in machine
        ATM atm = new ATM(bank, dispenser);
        AtmController controller = new AtmController();

        printDemoCards();

        // ------------------------------------------------------------------
        // FEATURE 1+2+3: Happy path — insert → PIN → balance → withdraw → eject
        // ------------------------------------------------------------------
        section("FEATURE 1+2+3 — Happy path (Alice's card)");
        safe(() -> {
            controller.insertCard(atm, "4111111111111111");
            controller.enterPin(atm, "1234");
            controller.checkBalance(atm);
            controller.withdraw(atm, 2_000);
            controller.displayStatus(atm);
            controller.ejectCard(atm);
        });
        controller.displayStatus(atm);

        // ------------------------------------------------------------------
        // Wrong PIN twice, then success
        // ------------------------------------------------------------------
        section("FAIL PATH — Wrong PIN twice, then correct PIN");
        safe(() -> {
            controller.insertCard(atm, "4222222222222222");
            controller.enterPin(atm, "0000");
        });
        safe(() -> controller.enterPin(atm, "1111"));
        safe(() -> {
            controller.enterPin(atm, "5678");
            controller.checkBalance(atm);
            controller.ejectCard(atm);
        });

        // ------------------------------------------------------------------
        // Insufficient account balance
        // ------------------------------------------------------------------
        section("FAIL PATH — Insufficient account balance");
        safe(() -> {
            controller.insertCard(atm, "4222222222222222");
            controller.enterPin(atm, "5678");
            controller.withdraw(atm, 999_999);
        });
        safe(() -> controller.ejectCard(atm));

        // ------------------------------------------------------------------
        // Insufficient ATM cash (machine has limited physical notes)
        // ------------------------------------------------------------------
        section("FAIL PATH — Insufficient ATM cash");
        safe(() -> {
            controller.insertCard(atm, "4333333333333333");
            controller.enterPin(atm, "9999");
            controller.withdraw(atm, 60_000); // more than dispenser's 50_000
        });
        safe(() -> controller.ejectCard(atm));

        // ------------------------------------------------------------------
        // PIN lockout after 3 failures
        // ------------------------------------------------------------------
        section("FAIL PATH — PIN lockout (3 wrong attempts)");
        safe(() -> {
            controller.insertCard(atm, "4111111111111111");
            controller.enterPin(atm, "0000");
            controller.enterPin(atm, "0000");
            controller.enterPin(atm, "0000");
        });
        controller.displayStatus(atm);

        // ------------------------------------------------------------------
        // Wrong state: withdraw while Idle
        // ------------------------------------------------------------------
        section("FAIL PATH — Withdraw while Idle (rejected by IdleState)");
        safe(() -> controller.withdraw(atm, 100));

        // ------------------------------------------------------------------
        // Invalid card
        // ------------------------------------------------------------------
        section("FAIL PATH — Invalid card number");
        safe(() -> controller.insertCard(atm, "9999999999999999"));

        // ------------------------------------------------------------------
        // Transaction history
        // ------------------------------------------------------------------
        section("Transaction history");
        controller.displayHistory(atm);

        System.out.println("\nDemo complete. Re-read states/ package — that is the heart of this LLD.");
        System.out.println("For hands-on practice, run: com.atmmachine.atm.demo.AtmDemo");
    }

    private static void printDemoCards() {
        System.out.println("Demo cards (from AccountFactory):");
        System.out.println("  4111111111111111 / PIN 1234  → Alice, balance 10000");
        System.out.println("  4222222222222222 / PIN 5678  → Bob,   balance 500");
        System.out.println("  4333333333333333 / PIN 9999  → Carol, balance 200000");
        System.out.println("  ATM cash in machine: 50000\n");
    }

    private static void section(String title) {
        System.out.println("\n>>> " + title);
        System.out.println("------------------------------------------------");
    }

    /**
     * Catch domain exceptions so one failed scenario does not kill the whole demo.
     */
    private static void safe(Runnable action) {
        try {
            action.run();
        } catch (RuntimeException ex) {
            System.out.println("!! " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }
}
