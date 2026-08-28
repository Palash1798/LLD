package com.zoomcar.carrental.paymentStrategies;

import java.math.BigDecimal;

public class WalletPayment implements PaymentStrategy {

    @Override
    public boolean pay(BigDecimal amount) {
        System.out.println("Paid " + amount + " via Wallet");
        return true;
    }
}
