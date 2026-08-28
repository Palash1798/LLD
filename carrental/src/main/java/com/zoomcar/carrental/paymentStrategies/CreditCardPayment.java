package com.zoomcar.carrental.paymentStrategies;

import java.math.BigDecimal;

public class CreditCardPayment implements PaymentStrategy {

    @Override
    public boolean pay(BigDecimal amount) {
        System.out.println("Paid " + amount + " via Credit Card");
        return true;
    }
}
