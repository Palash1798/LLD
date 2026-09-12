package com.paymentgateway.paymentgateway.factories;

import com.paymentgateway.paymentgateway.enums.PaymentMethod;
import com.paymentgateway.paymentgateway.exceptions.UnsupportedPaymentMethodException;
import com.paymentgateway.paymentgateway.strategies.CardPaymentStrategy;
import com.paymentgateway.paymentgateway.strategies.PaymentStrategy;
import com.paymentgateway.paymentgateway.strategies.UpiPaymentStrategy;
import com.paymentgateway.paymentgateway.strategies.WalletPaymentStrategy;

/**
 * Step 6 — FACTORY: maps PaymentMethod enum → PaymentStrategy instance.
 *
 * Adding Net Banking = new strategy class + one line here.
 * PaymentService stays unchanged (Open/Closed).
 *
 * Same pattern as Airline PaymentStrategyFactory.
 */
public class PaymentStrategyFactory {

    private final CardPaymentStrategy cardStrategy = new CardPaymentStrategy();
    private final UpiPaymentStrategy upiStrategy = new UpiPaymentStrategy();
    private final WalletPaymentStrategy walletStrategy = new WalletPaymentStrategy();

    public PaymentStrategy getStrategy(PaymentMethod method) {
        return switch (method) {
            case CARD -> cardStrategy;
            case UPI -> upiStrategy;
            case WALLET -> walletStrategy;
            default -> throw new UnsupportedPaymentMethodException(method);
        };
    }
}
