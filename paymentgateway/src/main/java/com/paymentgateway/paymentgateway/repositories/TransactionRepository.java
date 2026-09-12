package com.paymentgateway.paymentgateway.repositories;

import com.paymentgateway.paymentgateway.enums.TransactionStatus;
import com.paymentgateway.paymentgateway.models.PaymentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Step 4 — In-memory transaction store keyed by orderId.
 *
 * Each orderId maps to a list of payment attempts (chronological).
 */
public class TransactionRepository {

    private final Map<String, List<PaymentTransaction>> transactionsByOrder = new HashMap<>();

    public void save(PaymentTransaction transaction) {
        transactionsByOrder
                .computeIfAbsent(transaction.getOrderId(), ignored -> new ArrayList<>())
                .add(transaction);
    }

    public List<PaymentTransaction> findByOrderId(String orderId) {
        return new ArrayList<>(transactionsByOrder.getOrDefault(orderId, List.of()));
    }

    public Optional<PaymentTransaction> findLatestByOrderId(String orderId) {
        List<PaymentTransaction> txns = transactionsByOrder.get(orderId);
        if (txns == null || txns.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(txns.get(txns.size() - 1));
    }

    public Optional<PaymentTransaction> findLatestSuccessfulByOrderId(String orderId) {
        List<PaymentTransaction> txns = transactionsByOrder.get(orderId);
        if (txns == null) {
            return Optional.empty();
        }
        for (int i = txns.size() - 1; i >= 0; i--) {
            PaymentTransaction txn = txns.get(i);
            if (txn.getStatus() == TransactionStatus.SUCCESS) {
                return Optional.of(txn);
            }
        }
        return Optional.empty();
    }
}
