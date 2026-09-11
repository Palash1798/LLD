package com.atmmachine.atm.enums;

/**
 * Human-readable mirror of the current State class.
 * Useful for logging / display; actual behavior still lives in State objects.
 */
public enum AtmSessionStatus {
    IDLE,
    HAS_CARD,
    AUTHENTICATED,
    DISPENSING
}
