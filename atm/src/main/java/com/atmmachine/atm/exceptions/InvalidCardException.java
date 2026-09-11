package com.atmmachine.atm.exceptions;

public class InvalidCardException extends RuntimeException {

    public InvalidCardException(String cardNumber) {
        super("Invalid card number: " + cardNumber);
    }
}
