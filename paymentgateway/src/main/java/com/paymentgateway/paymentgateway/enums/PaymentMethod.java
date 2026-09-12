package com.paymentgateway.paymentgateway.enums;

/**
 * Step 1 — Supported payment channels.
 *
 * Each value maps to one {@link com.paymentgateway.paymentgateway.strategies.PaymentStrategy}
 * implementation via {@link com.paymentgateway.paymentgateway.factories.PaymentStrategyFactory}.
 */
public enum PaymentMethod {
    CARD,
    UPI,
    WALLET
}
