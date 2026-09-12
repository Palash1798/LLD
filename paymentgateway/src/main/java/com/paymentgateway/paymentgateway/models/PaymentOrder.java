package com.paymentgateway.paymentgateway.models;

import com.paymentgateway.paymentgateway.enums.OrderStatus;

/**
 * Step 2 — Payment intent created by a merchant.
 *
 * Aggregate root for checkout: one order per customer checkout session.
 * Amount is stored in paise/cents (integer) — never use float for money.
 */
public class PaymentOrder {

    private final String orderId;
    private final String merchantId;
    private final int amountCents;
    private final String currency;
    private final String idempotencyKey;
    private final String merchantOrderRef;
    private final long createdAt;
    private final long expiresAt;

    private OrderStatus status;

    public PaymentOrder(String orderId,
                        String merchantId,
                        int amountCents,
                        String currency,
                        OrderStatus status,
                        String idempotencyKey,
                        String merchantOrderRef,
                        long createdAt,
                        long expiresAt) {
        this.orderId = orderId;
        this.merchantId = merchantId;
        this.amountCents = amountCents;
        this.currency = currency;
        this.status = status;
        this.idempotencyKey = idempotencyKey;
        this.merchantOrderRef = merchantOrderRef;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public String getCurrency() {
        return currency;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getMerchantOrderRef() {
        return merchantOrderRef;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public boolean isExpired(long nowMillis) {
        return nowMillis > expiresAt;
    }

    @Override
    public String toString() {
        return "PaymentOrder{id='" + orderId + "', amountCents=" + amountCents
                + ", status=" + status + ", merchantRef='" + merchantOrderRef + "'}";
    }
}
