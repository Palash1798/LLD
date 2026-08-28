package com.zoomcar.carrental.factories;

import com.zoomcar.carrental.enums.PaymentMethod;
import com.zoomcar.carrental.paymentStrategies.CreditCardPayment;
import com.zoomcar.carrental.paymentStrategies.PaymentStrategy;
import com.zoomcar.carrental.paymentStrategies.UpiPayment;
import com.zoomcar.carrental.paymentStrategies.WalletPayment;

public class PaymentStrategyFactory {

    public static PaymentStrategy getPaymentStrategyByPayMethod(PaymentMethod paymentMethod) {

        if(paymentMethod == PaymentMethod.CREDIT_CARD){
            return new CreditCardPayment();
        } else if (paymentMethod == PaymentMethod.UPI) {
            return new UpiPayment();
        } else {
            return new WalletPayment();
        }
    }
}
