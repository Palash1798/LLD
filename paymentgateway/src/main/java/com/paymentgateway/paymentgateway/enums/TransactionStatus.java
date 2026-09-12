package com.paymentgateway.paymentgateway.enums;

/**
 * Step 1 — Lifecycle of a single payment attempt ({@link com.paymentgateway.paymentgateway.models.PaymentTransaction}).
 *
 * One order can have many transactions (retries after FAILED).
 *   INITIATED → PROCESSING → SUCCESS | FAILED
 */
public enum TransactionStatus {
    INITIATED,
    PROCESSING,
    SUCCESS,
    FAILED
}
