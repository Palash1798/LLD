package com.vendingmachine.vending.states;

import com.vendingmachine.vending.enums.MachineStatus;
import com.vendingmachine.vending.exceptions.InvalidOperationException;
import com.vendingmachine.vending.models.Product;
import com.vendingmachine.vending.models.Transaction;
import com.vendingmachine.vending.models.VendingMachine;

/**
 * DISPENSE = product already validated & selected; now give item + change.
 *
 * Invariant: only enter this state when selectedProduct != null and balance >= price.
 */
public class DispenseState implements VendingState {

    @Override
    public void insertMoney(VendingMachine machine, int amount) {
        throw new InvalidOperationException("Cannot insert money while dispensing.");
    }

    @Override
    public void selectProduct(VendingMachine machine, String code) {
        throw new InvalidOperationException("Already dispensing. Please wait.");
    }

    @Override
    public void dispense(VendingMachine machine) {
        Product product = machine.getSelectedProduct();
        if (product == null) {
            // Should not happen if transitions are correct — defensive check
            throw new InvalidOperationException("No product selected to dispense.");
        }

        int price = product.getPrice();
        int paid = machine.getBalance();
        int change = paid - price;

        // Step 1: reduce inventory (payment is committed on this path)
        machine.getInventory().decrement(product.getCode());
        System.out.println("[Dispense] Dispensed: " + product.getName()
                + " (" + product.getCode() + ")");

        // Step 2: return leftover money as numeric change (no coin-drawer algorithm in MVP)
        System.out.println("[Dispense] Paid=" + paid + ", price=" + price + ", change=" + change);

        // Step 3: optional history (like Move list in Chess)
        machine.addTransaction(new Transaction(product.getCode(), product.getName(), paid, change));

        // Step 4: clear session
        // If everything sold out → SoldOutState; else Idle
        if (machine.getInventory().isAllSoldOut()) {
            machine.clearSession();
            machine.setState(new SoldOutState());
            System.out.println("[Dispense] Inventory empty. State → SoldOut");
        } else {
            machine.resetToIdle();
            System.out.println("[Dispense] State → Idle");
        }
    }

    @Override
    public void cancel(VendingMachine machine) {
        // Physical machines rarely cancel mid-dispense; reject for clarity in interview.
        throw new InvalidOperationException("Cannot cancel while dispensing.");
    }

    @Override
    public MachineStatus getStatus() {
        return MachineStatus.DISPENSING;
    }
}
