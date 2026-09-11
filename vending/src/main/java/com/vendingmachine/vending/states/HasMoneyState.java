package com.vendingmachine.vending.states;

import com.vendingmachine.vending.enums.MachineStatus;
import com.vendingmachine.vending.exceptions.InsufficientFundsException;
import com.vendingmachine.vending.exceptions.InvalidOperationException;
import com.vendingmachine.vending.exceptions.InvalidProductCodeException;
import com.vendingmachine.vending.exceptions.OutOfStockException;
import com.vendingmachine.vending.models.ItemSlot;
import com.vendingmachine.vending.models.Product;
import com.vendingmachine.vending.models.VendingMachine;

/**
 * HAS_MONEY = customer has inserted some balance; can add more, select, or cancel.
 *
 * On valid select we:
 *   1) validate code / stock / funds
 *   2) remember selected product on machine
 *   3) move to DispenseState
 *   4) auto-call dispense() (simple interview flow)
 *
 * On failure we KEEP the money and STAY in HasMoney (user can insert more or cancel).
 */
public class HasMoneyState implements VendingState {

    @Override
    public void insertMoney(VendingMachine machine, int amount) {
        // Step 1: still allow topping up
        if (amount <= 0) {
            throw new InvalidOperationException("Insert amount must be positive. Got: " + amount);
        }
        machine.addBalance(amount);
        System.out.println("[HasMoney] Added " + amount + ". Balance=" + machine.getBalance());
        // stay in HasMoney — no state change
    }

    @Override
    public void selectProduct(VendingMachine machine, String code) {
        // Step 1: find slot by code
        ItemSlot slot = machine.getInventory().getSlot(code);
        if (slot == null) {
            throw new InvalidProductCodeException(code);
        }

        // Step 2: stock check (do NOT take money on failure)
        if (!slot.isAvailable()) {
            throw new OutOfStockException(code);
        }

        Product product = slot.getProduct();

        // Step 3: funds check
        if (machine.getBalance() < product.getPrice()) {
            throw new InsufficientFundsException(machine.getBalance(), product.getPrice());
        }

        // Step 4: commit selection on context
        machine.setSelectedProduct(product);
        System.out.println("[HasMoney] Selected " + product.getCode()
                + " (" + product.getName() + ") price=" + product.getPrice());

        // Step 5: transition HasMoney → Dispense
        machine.setState(new DispenseState());
        System.out.println("[HasMoney] State → Dispense");

        // Step 6: auto-dispense (common interview simplification)
        // Alternative: wait for an explicit dispense() button press.
        machine.dispense();
    }

    @Override
    public void dispense(VendingMachine machine) {
        throw new InvalidOperationException("Select a product before dispensing.");
    }

    @Override
    public void cancel(VendingMachine machine) {
        // Step 1: refund full balance
        int refund = machine.getBalance();
        System.out.println("[HasMoney] Cancelled. Refund=" + refund);

        // Step 2: clear money/selection and go Idle
        machine.resetToIdle();
        System.out.println("[HasMoney] State → Idle");
    }

    @Override
    public MachineStatus getStatus() {
        return MachineStatus.HAS_MONEY;
    }
}
