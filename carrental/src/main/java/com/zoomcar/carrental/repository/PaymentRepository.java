package com.zoomcar.carrental.repository;

import com.zoomcar.carrental.models.Payment;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class PaymentRepository {
    private final Map<Long, Payment> paymentTable = new TreeMap<>();
    private long previousId = 0L;

    public Payment savePayment(Payment payment) {
        previousId += 1;
        payment.setId(previousId);
        paymentTable.put(previousId, payment);
        return payment;
    }

    public Optional<Payment> findPaymentById(long paymentId) {
        return Optional.ofNullable(paymentTable.get(paymentId));
    }
}
