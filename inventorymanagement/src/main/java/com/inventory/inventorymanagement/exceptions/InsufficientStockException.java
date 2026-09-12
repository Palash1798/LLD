package com.inventory.inventorymanagement.exceptions;

/**
 * Thrown when available stock (onHand - reserved) is less than requested quantity.
 *
 * This is the core oversell guard — always check at RESERVE time, not at fulfill.
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String productId, int available, int requested) {
        super("Insufficient stock for " + productId
                + ": available=" + available + ", requested=" + requested);
    }
}
