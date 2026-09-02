package com.airline.airline.paymentStrategies;

import java.math.BigDecimal;

public interface PaymentStrategy {
    boolean pay(BigDecimal amount);
    boolean refund(BigDecimal amount);
}
