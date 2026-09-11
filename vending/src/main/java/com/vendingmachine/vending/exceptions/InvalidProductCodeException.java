package com.vendingmachine.vending.exceptions;

public class InvalidProductCodeException extends RuntimeException {

    public InvalidProductCodeException(String code) {
        super("Unknown product code: " + code);
    }
}
