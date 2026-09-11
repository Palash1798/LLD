package com.vendingmachine.vending.states;

import com.vendingmachine.vending.enums.MachineStatus;
import com.vendingmachine.vending.exceptions.InvalidOperationException;
import com.vendingmachine.vending.models.Product;
import com.vendingmachine.vending.models.VendingMachine;

/**
 * SOLD_OUT = every slot quantity is 0.
 * Most customer actions are rejected until admin restocks.
 */
public class SoldOutState implements VendingState {

    @Override
    public void insertMoney(VendingMachine machine, int amount) {
        throw new InvalidOperationException("Machine sold out. Cannot accept money.");
    }

    @Override
    public void selectProduct(VendingMachine machine, String code) {
        throw new InvalidOperationException("Machine sold out. Cannot select '" + code + "'.");
    }

    @Override
    public void dispense(VendingMachine machine) {
        throw new InvalidOperationException("Machine sold out. Nothing to dispense.");
    }

    @Override
    public void cancel(VendingMachine machine) {
        System.out.println("[SoldOut] Nothing to cancel.");
    }

    /**
     * Extension hook: admin restocks a product, then may leave SoldOut.
     */
    public void restock(VendingMachine machine, Product product, int quantity) {
        machine.getInventory().addProduct(product, quantity);
        System.out.println("[SoldOut] Restocked " + product.getCode() + " qty=" + quantity);
        if (!machine.getInventory().isAllSoldOut()) {
            machine.setState(new IdleState());
            System.out.println("[SoldOut] State → Idle");
        }
    }

    @Override
    public MachineStatus getStatus() {
        return MachineStatus.SOLD_OUT;
    }
}
