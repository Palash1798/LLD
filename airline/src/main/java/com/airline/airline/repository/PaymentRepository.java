package com.airline.airline.repository;

import com.airline.airline.models.Payment;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class PaymentRepository {
    private final Map<Long, Payment> paymentTable = new TreeMap<>();
    private final Map<String, Payment> idempotencyIndex = new TreeMap<>();
    private long previousId = 0L;

    public Payment save(Payment payment) {
        if (payment.getId() == 0) {
            previousId += 1;
            payment.setId(previousId);
        }
        paymentTable.put(payment.getId(), payment);
        if (payment.getIdempotencyKey() != null) {
            idempotencyIndex.put(payment.getIdempotencyKey(), payment);
        }
        return payment;
    }

    public Optional<Payment> findById(long id) {
        return Optional.ofNullable(paymentTable.get(id));
    }

    public Optional<Payment> findByIdempotencyKey(String key) {
        return Optional.ofNullable(idempotencyIndex.get(key));
    }
}
