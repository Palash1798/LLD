package com.airline.airline.paymentStrategies;

import java.math.BigDecimal;

public class WalletPayment implements PaymentStrategy {

    @Override
    public boolean pay(BigDecimal amount) {
        System.out.println("Paid " + amount + " via Wallet");
        return true;
    }

    @Override
    public boolean refund(BigDecimal amount) {
        System.out.println("Refunded " + amount + " to Wallet");
        return true;
    }
}
