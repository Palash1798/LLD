package com.inventory.inventorymanagement.exceptions;

/**
 * Thrown when orderId is not in the order store.
 */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String orderId) {
        super("Order not found: " + orderId);
    }
}
