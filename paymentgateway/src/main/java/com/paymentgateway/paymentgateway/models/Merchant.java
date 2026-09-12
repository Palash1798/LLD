package com.paymentgateway.paymentgateway.models;

/**
 * Step 2 — Merchant who integrates with the payment gateway (e.g. e-commerce store).
 *
 * In production: apiKey would be hashed; webhookUrl receives SUCCESS callbacks.
 */
public class Merchant {

    private final String merchantId;
    private final String name;
    private final String apiKey;
    private final String webhookUrl;

    public Merchant(String merchantId, String name, String apiKey, String webhookUrl) {
        this.merchantId = merchantId;
        this.name = name;
        this.apiKey = apiKey;
        this.webhookUrl = webhookUrl;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getName() {
        return name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    @Override
    public String toString() {
        return "Merchant{id='" + merchantId + "', name='" + name + "'}";
    }
}
