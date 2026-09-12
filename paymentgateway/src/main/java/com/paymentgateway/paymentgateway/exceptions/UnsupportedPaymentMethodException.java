package com.paymentgateway.paymentgateway.exceptions;

import com.paymentgateway.paymentgateway.enums.PaymentMethod;

public class UnsupportedPaymentMethodException extends RuntimeException {

    public UnsupportedPaymentMethodException(PaymentMethod method) {
        super("Unsupported payment method: " + method);
    }
}
