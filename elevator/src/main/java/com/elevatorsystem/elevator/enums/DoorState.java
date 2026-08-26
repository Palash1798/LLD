package com.elevatorsystem.elevator.enums;

/**
 * Simplified door model for 1-hour LLD.
 * OPEN for one tick when we serve a floor, then CLOSED.
 */
public enum DoorState {
    OPEN,
    CLOSED
}
