package com.paymentgateway.paymentgateway.models;

/**
 * Step 2 — Return value from a {@link com.paymentgateway.paymentgateway.strategies.PaymentStrategy}.
 *
 * Wraps success/failure from mock bank/UPI processor so PaymentService can update order state.
 */
public class PaymentResult {

    private final boolean success;
    private final String processorRef;
    private final String message;

    private PaymentResult(boolean success, String processorRef, String message) {
        this.success = success;
        this.processorRef = processorRef;
        this.message = message;
    }

    public static PaymentResult success(String processorRef) {
        return new PaymentResult(true, processorRef, null);
    }

    public static PaymentResult failed(String message) {
        return new PaymentResult(false, null, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getProcessorRef() {
        return processorRef;
    }

    public String getMessage() {
        return message;
    }
}
