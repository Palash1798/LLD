package com.paymentgateway.paymentgateway.strategies;

import com.paymentgateway.paymentgateway.models.PaymentInstrumentDetails;
import com.paymentgateway.paymentgateway.models.PaymentResult;

/**
 * Step 5 — STRATEGY PATTERN (core of this LLD).
 *
 * Each payment method (Card, UPI, Wallet) implements this interface.
 * PaymentService never uses if-else on method type — it asks the factory for the strategy.
 *
 * Same idea as Airline {@code PaymentStrategy} — Open/Closed principle.
 */
public interface PaymentStrategy {

    /**
     * Charge the customer via external processor (mocked in interview).
     */
    PaymentResult pay(int amountCents, PaymentInstrumentDetails details);

    /**
     * Refund a previously successful charge (extension — used by refund flow).
     */
    PaymentResult refund(int amountCents, String originalProcessorRef);
}
