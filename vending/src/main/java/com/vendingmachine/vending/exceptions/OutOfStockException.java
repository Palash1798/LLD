package com.vendingmachine.vending.exceptions;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String code) {
        super("Product out of stock: " + code);
    }
}
