package com.paymentgateway.paymentgateway.strategies;

import com.paymentgateway.paymentgateway.models.PaymentInstrumentDetails;
import com.paymentgateway.paymentgateway.models.PaymentResult;

import java.util.UUID;

/**
 * Step 5 — Concrete strategy for credit/debit card payments.
 *
 * In production this would call Visa/Mastercard acquirer APIs.
 * Demo validates basic fields and returns a mock processor reference.
 */
public class CardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult pay(int amountCents, PaymentInstrumentDetails details) {
        // Step 5a: validate instrument
        if (details.getCardNumber() == null || details.getCardNumber().isBlank()) {
            return PaymentResult.failed("Card number is required");
        }
        if (details.getCvv() == null || details.getCvv().isBlank()) {
            return PaymentResult.failed("CVV is required");
        }
        if (details.getCardNumber().length() < 12) {
            return PaymentResult.failed("Invalid card number");
        }

        // Step 5b: simulate bank decline (for demo failure paths)
        if (details.isSimulateFailure()) {
            return PaymentResult.failed("Bank declined the card");
        }

        // Step 5c: mock external processor call
        String processorRef = "CARD-" + UUID.randomUUID();
        String last4 = details.getCardNumber().substring(details.getCardNumber().length() - 4);
        System.out.println("[CardProcessor] Charged " + amountCents + " paise on card ending " + last4
                + " | ref=" + processorRef);

        return PaymentResult.success(processorRef);
    }

    @Override
    public PaymentResult refund(int amountCents, String originalProcessorRef) {
        String refundRef = "CARD-REF-" + UUID.randomUUID();
        System.out.println("[CardProcessor] Refunded " + amountCents + " paise to " + originalProcessorRef
                + " | ref=" + refundRef);
        return PaymentResult.success(refundRef);
    }
}
