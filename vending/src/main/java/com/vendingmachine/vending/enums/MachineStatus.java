package com.vendingmachine.vending.enums;

/**
 * Human-readable mirror of current State class.
 * Useful for logging / display; actual behavior still lives in State objects.
 */
public enum MachineStatus {
    IDLE,
    HAS_MONEY,
    DISPENSING,
    SOLD_OUT
}
