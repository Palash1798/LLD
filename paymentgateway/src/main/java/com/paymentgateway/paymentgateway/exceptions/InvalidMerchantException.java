package com.paymentgateway.paymentgateway.exceptions;

public class InvalidMerchantException extends RuntimeException {

    public InvalidMerchantException(String message) {
        super(message);
    }
}
