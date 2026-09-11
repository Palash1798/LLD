package com.vendingmachine.vending.models;

import com.vendingmachine.vending.enums.Coin;
import com.vendingmachine.vending.enums.MachineStatus;
import com.vendingmachine.vending.states.IdleState;
import com.vendingmachine.vending.states.SoldOutState;
import com.vendingmachine.vending.states.VendingState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CONTEXT in the State pattern.
 *
 * Holds shared data (inventory, balance, selected product, history)
 * and delegates every user action to currentState.
 *
 * Interview one-liner:
 * "Machine never switches on status — it asks the current state object what to do."
 */
public class VendingMachine {

    private final Inventory inventory;
    private int balance;
    private VendingState currentState;
    private Product selectedProduct;
    private final List<Transaction> history = new ArrayList<>();

    public VendingMachine(Inventory inventory) {
        this.inventory = inventory;
        this.balance = 0;
        this.selectedProduct = null;

        // Step 1: start Idle, unless already empty stock
        if (inventory.isAllSoldOut()) {
            this.currentState = new SoldOutState();
        } else {
            this.currentState = new IdleState();
        }
    }

    // -------------------------------------------------------------------------
    // Public API — always delegate to current state
    // -------------------------------------------------------------------------

    /**
     * Feature: insert cash (integer units).
     */
    public void insertMoney(int amount) {
        currentState.insertMoney(this, amount);
    }

    /**
     * Convenience: insert a Coin denomination.
     */
    public void insertCoin(Coin coin) {
        insertMoney(coin.getValue());
    }

    /**
     * Feature: select product by code (e.g. "A1").
     * On success, HasMoneyState auto-triggers dispense.
     */
    public void selectProduct(String code) {
        currentState.selectProduct(this, code);
    }

    /**
     * Usually called by HasMoneyState after a valid select.
     * Can also be called explicitly if you prefer two-step UX.
     */
    public void dispense() {
        currentState.dispense(this);
    }

    /**
     * Feature: cancel and refund full balance.
     */
    public void cancel() {
        currentState.cancel(this);
    }

    // -------------------------------------------------------------------------
    // Helpers used BY states (context API)
    // -------------------------------------------------------------------------

    public void setState(VendingState state) {
        this.currentState = state;
    }

    public VendingState getCurrentState() {
        return currentState;
    }

    public MachineStatus getStatus() {
        return currentState.getStatus();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getBalance() {
        return balance;
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public void addTransaction(Transaction transaction) {
        history.add(transaction);
    }

    public List<Transaction> getHistory() {
        return Collections.unmodifiableList(history);
    }

    /**
     * Clear money + selection, but do not change state.
     * Used when moving to SoldOut after last item.
     */
    public void clearSession() {
        this.balance = 0;
        this.selectedProduct = null;
    }

    /**
     * Full session reset back to Idle (after dispense or cancel).
     */
    public void resetToIdle() {
        clearSession();
        this.currentState = new IdleState();
    }

    public void displayInventory() {
        inventory.display();
        System.out.println("Status=" + getStatus() + ", Balance=" + balance);
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
