package com.paymentgateway.paymentgateway.dto;

/**
 * Step 3 — API input when merchant creates a payment order.
 */
public class CreateOrderRequest {

    private final String merchantId;
    private final String apiKey;
    private final int amountCents;
    private final String idempotencyKey;
    private final String merchantOrderRef;

    public CreateOrderRequest(String merchantId,
                              String apiKey,
                              int amountCents,
                              String idempotencyKey,
                              String merchantOrderRef) {
        this.merchantId = merchantId;
        this.apiKey = apiKey;
        this.amountCents = amountCents;
        this.idempotencyKey = idempotencyKey;
        this.merchantOrderRef = merchantOrderRef;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getMerchantOrderRef() {
        return merchantOrderRef;
    }
}
