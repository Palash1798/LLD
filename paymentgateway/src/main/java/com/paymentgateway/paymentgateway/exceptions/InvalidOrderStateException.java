package com.paymentgateway.paymentgateway.exceptions;

import com.paymentgateway.paymentgateway.enums.OrderStatus;

public class InvalidOrderStateException extends RuntimeException {

    public InvalidOrderStateException(String orderId, OrderStatus current, String action) {
        super("Cannot " + action + " on order " + orderId + " — current status is " + current);
    }
}
