package com.paymentgateway.paymentgateway.models;

import com.paymentgateway.paymentgateway.enums.PaymentMethod;
import com.paymentgateway.paymentgateway.enums.TransactionStatus;

/**
 * Step 2 — One payment processor attempt against an order.
 *
 * Why separate from PaymentOrder?
 *   - Retries: failed CARD attempt, then successful UPI attempt = 2 transaction rows
 *   - Refunds: new transaction row with type REFUND (extension)
 *   - Audit trail for merchant support
 */
public class PaymentTransaction {

    private final String txnId;
    private final String orderId;
    private final PaymentMethod method;
    private final int amountCents;
    private final long createdAt;

    private TransactionStatus status;
    private String processorRef;
    private String failureReason;

    public PaymentTransaction(String txnId,
                              String orderId,
                              PaymentMethod method,
                              TransactionStatus status,
                              int amountCents,
                              long createdAt) {
        this.txnId = txnId;
        this.orderId = orderId;
        this.method = method;
        this.status = status;
        this.amountCents = amountCents;
        this.createdAt = createdAt;
    }

    public String getTxnId() {
        return txnId;
    }

    public String getOrderId() {
        return orderId;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public String getProcessorRef() {
        return processorRef;
    }

    public void setProcessorRef(String processorRef) {
        this.processorRef = processorRef;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "PaymentTransaction{txnId='" + txnId + "', method=" + method
                + ", status=" + status + ", processorRef='" + processorRef + "'}";
    }
}
