package com.paymentgateway.paymentgateway.dto;

import com.paymentgateway.paymentgateway.enums.OrderStatus;

/**
 * Step 3 — API output after order creation.
 */
public class CreateOrderResponse {

    private final String orderId;
    private final OrderStatus status;
    private final int amountCents;
    private final String currency;
    private final long expiresAt;

    public CreateOrderResponse(String orderId,
                               OrderStatus status,
                               int amountCents,
                               String currency,
                               long expiresAt) {
        this.orderId = orderId;
        this.status = status;
        this.amountCents = amountCents;
        this.currency = currency;
        this.expiresAt = expiresAt;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public String getCurrency() {
        return currency;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    @Override
    public String toString() {
        return "CreateOrderResponse{orderId='" + orderId + "', status=" + status
                + ", amountCents=" + amountCents + "}";
    }
}
