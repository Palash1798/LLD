package com.atmmachine.atm.exceptions;

public class InvalidPinException extends RuntimeException {

    public InvalidPinException(int attemptsLeft) {
        super("Incorrect PIN. Attempts remaining: " + attemptsLeft);
    }
}
