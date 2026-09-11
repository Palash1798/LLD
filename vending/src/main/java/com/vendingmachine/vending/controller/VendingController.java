package com.vendingmachine.vending.controller;

import com.vendingmachine.vending.models.VendingMachine;

/**
 * Thin controller (same idea as GameController in Chess / Snake & Ladder).
 * Keeps CLI / API layer free of business rules — Machine + States own the logic.
 */
public class VendingController {

    /**
     * Feature 1 helper: show products.
     */
    public void displayInventory(VendingMachine machine) {
        machine.displayInventory();
    }

    /**
     * Feature 2: accept money.
     */
    public void insertMoney(VendingMachine machine, int amount) {
        machine.insertMoney(amount);
    }

    /**
     * Feature 2+3: select product (validates + dispenses via State).
     */
    public void selectProduct(VendingMachine machine, String code) {
        machine.selectProduct(code);
    }

    /**
     * Feature 3: cancel / refund.
     */
    public void cancel(VendingMachine machine) {
        machine.cancel();
    }

    public void displayHistory(VendingMachine machine) {
        machine.displayHistory();
    }
}
