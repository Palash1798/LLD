package com.paymentgateway.paymentgateway.enums;

/**
 * Step 1 — Business lifecycle of a {@link com.paymentgateway.paymentgateway.models.PaymentOrder}.
 *
 * State transitions (see LLD_PAYMENT_GATEWAY.md §6.4):
 *   CREATED → PAID | EXPIRED | FAILED (optional)
 *   PAID    → REFUNDED
 */
public enum OrderStatus {
    CREATED,
    PAID,
    FAILED,
    EXPIRED,
    REFUNDED
}
