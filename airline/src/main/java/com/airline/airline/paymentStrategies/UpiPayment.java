package com.airline.airline.paymentStrategies;

import java.math.BigDecimal;

public class UpiPayment implements PaymentStrategy {

    @Override
    public boolean pay(BigDecimal amount) {
        System.out.println("Paid " + amount + " via UPI");
        return true;
    }

    @Override
    public boolean refund(BigDecimal amount) {
        System.out.println("Refunded " + amount + " to UPI");
        return true;
    }
}
