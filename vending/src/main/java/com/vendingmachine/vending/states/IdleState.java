package com.vendingmachine.vending.states;

import com.vendingmachine.vending.enums.MachineStatus;
import com.vendingmachine.vending.exceptions.InvalidOperationException;
import com.vendingmachine.vending.models.VendingMachine;

/**
 * IDLE = waiting for customer money. No product selected. Balance should be 0.
 *
 * Allowed: insertMoney → go to HasMoney
 * Rejected: selectProduct, dispense
 * Cancel: soft no-op
 */
public class IdleState implements VendingState {

    @Override
    public void insertMoney(VendingMachine machine, int amount) {
        // Step 1: reject non-positive amounts
        if (amount <= 0) {
            throw new InvalidOperationException("Insert amount must be positive. Got: " + amount);
        }

        // Step 2: add money onto the machine (context holds shared data)
        machine.addBalance(amount);
        System.out.println("[Idle] Inserted " + amount + ". Balance=" + machine.getBalance());

        // Step 3: transition Idle → HasMoney
        machine.setState(new HasMoneyState());
        System.out.println("[Idle] State → HasMoney");
    }

    @Override
    public void selectProduct(VendingMachine machine, String code) {
        throw new InvalidOperationException(
                "Cannot select '" + code + "' in Idle. Insert money first.");
    }

    @Override
    public void dispense(VendingMachine machine) {
        throw new InvalidOperationException("Cannot dispense in Idle. Insert money and select a product.");
    }

    @Override
    public void cancel(VendingMachine machine) {
        // Nothing to refund — already idle
        System.out.println("[Idle] Nothing to cancel.");
    }

    @Override
    public MachineStatus getStatus() {
        return MachineStatus.IDLE;
    }
}
