package com.vendingmachine.vending;

import com.vendingmachine.vending.enums.MachineStatus;
import com.vendingmachine.vending.exceptions.InsufficientFundsException;
import com.vendingmachine.vending.exceptions.InvalidOperationException;
import com.vendingmachine.vending.factories.ProductFactory;
import com.vendingmachine.vending.models.VendingMachine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Lightweight checks for the 3 MVP features (no Spring context needed).
 */
class VendingApplicationTests {

    private VendingMachine machine;

    @BeforeEach
    void setUp() {
        machine = new VendingMachine(ProductFactory.createDefaultInventory());
    }

    @Test
    void happyPath_dispenseAndReturnChange() {
        machine.insertMoney(50);
        assertEquals(MachineStatus.HAS_MONEY, machine.getStatus());

        machine.selectProduct("A1"); // price 25 → change 25, back to Idle

        assertEquals(MachineStatus.IDLE, machine.getStatus());
        assertEquals(0, machine.getBalance());
        assertEquals(4, machine.getInventory().getSlot("A1").getQuantity());
        assertEquals(1, machine.getHistory().size());
    }

    @Test
    void insufficientFunds_keepsMoney() {
        machine.insertMoney(10);
        assertThrows(InsufficientFundsException.class, () -> machine.selectProduct("A1"));
        assertEquals(10, machine.getBalance());
        assertEquals(MachineStatus.HAS_MONEY, machine.getStatus());
    }

    @Test
    void cancel_refundsAndReturnsToIdle() {
        machine.insertMoney(40);
        machine.cancel();
        assertEquals(0, machine.getBalance());
        assertEquals(MachineStatus.IDLE, machine.getStatus());
    }

    @Test
    void selectWhileIdle_rejected() {
        assertThrows(InvalidOperationException.class, () -> machine.selectProduct("A1"));
    }
}
