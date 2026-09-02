package com.airline.airline.paymentStrategies;

import java.math.BigDecimal;

public class CreditCardPayment implements PaymentStrategy {

    @Override
    public boolean pay(BigDecimal amount) {
        System.out.println("Paid " + amount + " via Credit Card");
        return true;
    }

    @Override
    public boolean refund(BigDecimal amount) {
        System.out.println("Refunded " + amount + " to Credit Card");
        return true;
    }
}
