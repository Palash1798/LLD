package com.paymentgateway.paymentgateway.dto;

import com.paymentgateway.paymentgateway.enums.OrderStatus;
import com.paymentgateway.paymentgateway.enums.PaymentMethod;
import com.paymentgateway.paymentgateway.enums.TransactionStatus;
import com.paymentgateway.paymentgateway.models.PaymentOrder;
import com.paymentgateway.paymentgateway.models.PaymentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Step 3 — Combined view of order + latest transaction + full attempt history.
 */
public class PaymentStatusResponse {

    private final String orderId;
    private final OrderStatus orderStatus;
    private final int amountCents;
    private final String currency;
    private final TransactionStatus latestTxnStatus;
    private final PaymentMethod latestPaymentMethod;
    private final String processorRef;
    private final String failureReason;
    private final List<String> transactionHistory;

    public PaymentStatusResponse(String orderId,
                                 OrderStatus orderStatus,
                                 int amountCents,
                                 String currency,
                                 TransactionStatus latestTxnStatus,
                                 PaymentMethod latestPaymentMethod,
                                 String processorRef,
                                 String failureReason,
                                 List<String> transactionHistory) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.amountCents = amountCents;
        this.currency = currency;
        this.latestTxnStatus = latestTxnStatus;
        this.latestPaymentMethod = latestPaymentMethod;
        this.processorRef = processorRef;
        this.failureReason = failureReason;
        this.transactionHistory = transactionHistory;
    }

    public static PaymentStatusResponse from(PaymentOrder order,
                                           List<PaymentTransaction> transactions) {
        PaymentTransaction latest = transactions.isEmpty() ? null : transactions.get(transactions.size() - 1);

        List<String> history = new ArrayList<>();
        for (PaymentTransaction txn : transactions) {
            history.add(txn.getTxnId() + " | " + txn.getMethod() + " | " + txn.getStatus()
                    + (txn.getFailureReason() != null ? " | " + txn.getFailureReason() : ""));
        }

        return new PaymentStatusResponse(
                order.getOrderId(),
                order.getStatus(),
                order.getAmountCents(),
                order.getCurrency(),
                latest != null ? latest.getStatus() : null,
                latest != null ? latest.getMethod() : null,
                latest != null ? latest.getProcessorRef() : null,
                latest != null ? latest.getFailureReason() : null,
                history
        );
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public String getCurrency() {
        return currency;
    }

    public TransactionStatus getLatestTxnStatus() {
        return latestTxnStatus;
    }

    public PaymentMethod getLatestPaymentMethod() {
        return latestPaymentMethod;
    }

    public String getProcessorRef() {
        return processorRef;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    @Override
    public String toString() {
        return "PaymentStatusResponse{orderId='" + orderId + "', orderStatus=" + orderStatus
                + ", latestTxnStatus=" + latestTxnStatus + ", processorRef='" + processorRef + "'}";
    }
}
