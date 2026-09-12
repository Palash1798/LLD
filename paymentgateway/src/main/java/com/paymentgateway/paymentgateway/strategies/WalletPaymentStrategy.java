package com.paymentgateway.paymentgateway.strategies;

import com.paymentgateway.paymentgateway.models.PaymentInstrumentDetails;
import com.paymentgateway.paymentgateway.models.PaymentResult;

import java.util.UUID;

/**
 * Step 5 — Concrete strategy for wallet balance debit (Paytm wallet, etc.).
 *
 * Extension beyond MVP — included so Factory pattern is complete.
 */
public class WalletPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult pay(int amountCents, PaymentInstrumentDetails details) {
        if (details.getWalletId() == null || details.getWalletId().isBlank()) {
            return PaymentResult.failed("Wallet ID is required");
        }
        if (details.isSimulateFailure()) {
            return PaymentResult.failed("Insufficient wallet balance");
        }

        String processorRef = "WALLET-" + UUID.randomUUID();
        System.out.println("[WalletProcessor] Debited " + amountCents + " paise from wallet "
                + details.getWalletId() + " | ref=" + processorRef);

        return PaymentResult.success(processorRef);
    }

    @Override
    public PaymentResult refund(int amountCents, String originalProcessorRef) {
        String refundRef = "WALLET-REF-" + UUID.randomUUID();
        System.out.println("[WalletProcessor] Credited " + amountCents + " paise | ref=" + refundRef);
        return PaymentResult.success(refundRef);
    }
}
