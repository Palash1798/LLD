package com.zoomcar.carrental.exceptions;

public class PaymentFailedException extends Exception {
    public PaymentFailedException(String message) {
        super(message);
    }
}
