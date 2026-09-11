package com.atmmachine.atm.exceptions;

/**
 * Thrown when the user tries an action that is illegal in the current state
 * (e.g. withdraw before PIN verification).
 */
public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(message);
    }
}
