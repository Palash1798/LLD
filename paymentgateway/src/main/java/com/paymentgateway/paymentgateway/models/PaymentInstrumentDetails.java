package com.paymentgateway.paymentgateway.models;

/**
 * Step 2 — Customer payment instrument sent per checkout (NOT persisted in MVP).
 *
 * Production: never store CVV; use tokenized card references from PCI vault.
 * Demo flag {@code simulateFailure} lets us test FAILED paths without real banks.
 */
public class PaymentInstrumentDetails {

    private String cardNumber;
    private String cvv;
    private String upiId;
    private String walletId;
    private boolean simulateFailure;

    public static PaymentInstrumentDetails card(String cardNumber, String cvv) {
        PaymentInstrumentDetails details = new PaymentInstrumentDetails();
        details.cardNumber = cardNumber;
        details.cvv = cvv;
        return details;
    }

    public static PaymentInstrumentDetails upi(String upiId) {
        PaymentInstrumentDetails details = new PaymentInstrumentDetails();
        details.upiId = upiId;
        return details;
    }

    public static PaymentInstrumentDetails wallet(String walletId) {
        PaymentInstrumentDetails details = new PaymentInstrumentDetails();
        details.walletId = walletId;
        return details;
    }

    public static PaymentInstrumentDetails failingCard(String cardNumber, String cvv) {
        PaymentInstrumentDetails details = card(cardNumber, cvv);
        details.simulateFailure = true;
        return details;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public String getUpiId() {
        return upiId;
    }

    public String getWalletId() {
        return walletId;
    }

    public boolean isSimulateFailure() {
        return simulateFailure;
    }
}
