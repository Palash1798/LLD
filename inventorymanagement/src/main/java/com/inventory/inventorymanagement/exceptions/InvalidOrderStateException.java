package com.inventory.inventorymanagement.exceptions;

import com.inventory.inventorymanagement.enums.OrderStatus;

/**
 * Thrown when an operation is illegal for the order's current status
 * (e.g. fulfill a CANCELLED order).
 */
public class InvalidOrderStateException extends RuntimeException {

    public InvalidOrderStateException(String orderId, OrderStatus current, String operation) {
        super("Cannot " + operation + " order " + orderId + " in status " + current);
    }
}
