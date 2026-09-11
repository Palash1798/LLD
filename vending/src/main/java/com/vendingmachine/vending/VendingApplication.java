package com.vendingmachine.vending;

import com.vendingmachine.vending.controller.VendingController;
import com.vendingmachine.vending.factories.ProductFactory;
import com.vendingmachine.vending.models.Inventory;
import com.vendingmachine.vending.models.VendingMachine;

/**
 * Entry point of the application.
 *
 * Delegates to so you can practice the State-pattern
 * flow without needing the Spring web stack.
 *
 *  Walks through the 3 MVP features from LLD_VENDING_MACHINE.md:
 *   1) Display inventory
 *   2) Insert money + select (validation)
 *   3) Dispense + change, and cancel/refund
 *
 * IDE: run this class (or VendingMachineDemo).
 */
public class VendingApplication {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  VENDING MACHINE LLD — STUDY DEMO");
        System.out.println("  Pattern: State (Idle/HasMoney/Dispense)");
        System.out.println("========================================\n");

        // Step 0: wire objects (like interview setup)
        Inventory inventory = ProductFactory.createDefaultInventory();
        VendingMachine machine = new VendingMachine(inventory);
        VendingController controller = new VendingController();

        // ------------------------------------------------------------------
        // FEATURE 1: Display inventory
        // ------------------------------------------------------------------
        section("FEATURE 1 — Display inventory");
        controller.displayInventory(machine);

        // ------------------------------------------------------------------
        // FEATURE 2+3: Happy path — insert → select → auto dispense + change
        // ------------------------------------------------------------------
        section("FEATURE 2+3 — Happy path (Coke A1 price=25)");
        safe(() -> {
            // Insert more than price to show change
            controller.insertMoney(machine, 50);
            controller.selectProduct(machine, "A1");
        });
        controller.displayInventory(machine);

        // ------------------------------------------------------------------
        // Insufficient funds — keep money, stay HasMoney
        // ------------------------------------------------------------------
        section("FAIL PATH — Insufficient funds then top-up");
        safe(() -> {
            controller.insertMoney(machine, 10);
            controller.selectProduct(machine, "A2"); // Pepsi=35 → should fail
        });
        System.out.println("Balance still kept: " + machine.getBalance()
                + ", status=" + machine.getStatus());
        safe(() -> {
            controller.insertMoney(machine, 30);     // now 40 >= 35
            controller.selectProduct(machine, "A2");
        });

        // ------------------------------------------------------------------
        // Cancel / refund
        // ------------------------------------------------------------------
        section("FEATURE 3 — Cancel / refund");
        safe(() -> {
            controller.insertMoney(machine, 40);
            System.out.println("Before cancel: balance=" + machine.getBalance()
                    + ", status=" + machine.getStatus());
            controller.cancel(machine);
            System.out.println("After cancel: balance=" + machine.getBalance()
                    + ", status=" + machine.getStatus());
        });

        // ------------------------------------------------------------------
        // Wrong state: select while Idle
        // ------------------------------------------------------------------
        section("FAIL PATH — Select while Idle (rejected by IdleState)");
        safe(() -> controller.selectProduct(machine, "B1"));

        // ------------------------------------------------------------------
        // Unknown code / out of stock style validation
        // ------------------------------------------------------------------
        section("FAIL PATH — Unknown product code");
        safe(() -> {
            controller.insertMoney(machine, 100);
            controller.selectProduct(machine, "Z9");
        });
        // refund leftover from failed attempt
        safe(() -> controller.cancel(machine));

        // ------------------------------------------------------------------
        // Transaction history
        // ------------------------------------------------------------------
        section("Transaction history");
        controller.displayHistory(machine);

        System.out.println("\nDemo complete. Re-read states/ package — that is the heart of this LLD.");
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
