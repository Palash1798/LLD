package com.paymentgateway.paymentgateway.exceptions;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String orderId) {
        super("Payment order not found: " + orderId);
    }
}
