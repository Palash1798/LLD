package com.zoomcar.carrental.paymentStrategies;

import java.math.BigDecimal;

public interface PaymentStrategy {
    boolean pay(BigDecimal amount);
}
