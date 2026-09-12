package com.paymentgateway.paymentgateway.strategies;

import com.paymentgateway.paymentgateway.models.PaymentInstrumentDetails;
import com.paymentgateway.paymentgateway.models.PaymentResult;

import java.util.UUID;

/**
 * Step 5 — Concrete strategy for UPI collect/pay.
 *
 * In production this would integrate with NPCI / PSP APIs.
 */
public class UpiPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult pay(int amountCents, PaymentInstrumentDetails details) {
        // Step 5a: validate UPI VPA format
        if (details.getUpiId() == null || details.getUpiId().isBlank()) {
            return PaymentResult.failed("UPI ID is required");
        }
        if (!details.getUpiId().contains("@")) {
            return PaymentResult.failed("Invalid UPI ID format");
        }

        // Step 5b: simulate failure flag (shared with card for demo)
        if (details.isSimulateFailure()) {
            return PaymentResult.failed("UPI collect request declined");
        }

        // Step 5c: mock NPCI collect
        String processorRef = "UPI-" + UUID.randomUUID();
        System.out.println("[UpiProcessor] Collected " + amountCents + " paise from " + details.getUpiId()
                + " | ref=" + processorRef);

        return PaymentResult.success(processorRef);
    }

    @Override
    public PaymentResult refund(int amountCents, String originalProcessorRef) {
        String refundRef = "UPI-REF-" + UUID.randomUUID();
        System.out.println("[UpiProcessor] Refunded " + amountCents + " paise for " + originalProcessorRef
                + " | ref=" + refundRef);
        return PaymentResult.success(refundRef);
    }
}
