package com.vendingmachine.vending.exceptions;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(int balance, int price) {
        super("Insufficient funds. Balance=" + balance + ", price=" + price
                + ". Insert more money or cancel.");
    }
}
