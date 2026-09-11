package com.vendingmachine.vending.exceptions;

/**
 * Thrown when user tries an action that is illegal in the current state
 * (e.g. select product while Idle / no money inserted).
 */
public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(message);
    }
}
