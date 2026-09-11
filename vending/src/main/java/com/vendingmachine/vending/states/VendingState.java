package com.vendingmachine.vending.states;

import com.vendingmachine.vending.enums.MachineStatus;
import com.vendingmachine.vending.models.VendingMachine;

/**
 * STATE PATTERN — State interface.
 *
 * Each concrete state decides what insert / select / dispense / cancel mean.
 * VendingMachine (Context) just delegates here — no giant if-else on status.
 *
 * Transitions (happy path):
 *   Idle --insertMoney--> HasMoney --valid select--> Dispense --done--> Idle
 */
public interface VendingState {

    void insertMoney(VendingMachine machine, int amount);

    void selectProduct(VendingMachine machine, String code);

    void dispense(VendingMachine machine);

    void cancel(VendingMachine machine);

    /** For display / logging — which logical status am I? */
    MachineStatus getStatus();
}
