package com.paymentgateway.paymentgateway.repositories;

import com.paymentgateway.paymentgateway.models.PaymentOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Step 4 — In-memory order store + idempotency index.
 *
 * idempotencyIndex key format: "merchantId:idempotencyKey" → orderId
 * Prevents duplicate orders when merchant retries HTTP createOrder.
 */
public class OrderRepository {

    private final Map<String, PaymentOrder> orders = new HashMap<>();
    private final Map<String, String> idempotencyIndex = new HashMap<>();

    public void save(PaymentOrder order) {
        orders.put(order.getOrderId(), order);
        String idempotencyKey = buildIdempotencyKey(order.getMerchantId(), order.getIdempotencyKey());
        idempotencyIndex.put(idempotencyKey, order.getOrderId());
    }

    public Optional<PaymentOrder> findById(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    public Optional<PaymentOrder> findByIdempotencyKey(String merchantId, String idempotencyKey) {
        String key = buildIdempotencyKey(merchantId, idempotencyKey);
        String orderId = idempotencyIndex.get(key);
        if (orderId == null) {
            return Optional.empty();
        }
        return findById(orderId);
    }

    public List<PaymentOrder> findByMerchantId(String merchantId) {
        List<PaymentOrder> result = new ArrayList<>();
        for (PaymentOrder order : orders.values()) {
            if (order.getMerchantId().equals(merchantId)) {
                result.add(order);
            }
        }
        return result;
    }

    private String buildIdempotencyKey(String merchantId, String idempotencyKey) {
        return merchantId + ":" + idempotencyKey;
    }
}
