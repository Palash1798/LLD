package com.airline.airline.factories;

import com.airline.airline.enums.PaymentMethod;
import com.airline.airline.paymentStrategies.CreditCardPayment;
import com.airline.airline.paymentStrategies.PaymentStrategy;
import com.airline.airline.paymentStrategies.UpiPayment;
import com.airline.airline.paymentStrategies.WalletPayment;

public class PaymentStrategyFactory {

    public static PaymentStrategy getStrategy(PaymentMethod method) {
        if (method == PaymentMethod.CREDIT_CARD) {
            return new CreditCardPayment();
        } else if (method == PaymentMethod.UPI) {
            return new UpiPayment();
        } else {
            return new WalletPayment();
        }
    }
}
