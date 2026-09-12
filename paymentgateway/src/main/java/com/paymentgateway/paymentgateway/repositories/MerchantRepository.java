package com.paymentgateway.paymentgateway.repositories;

import com.paymentgateway.paymentgateway.models.Merchant;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Step 4 — In-memory merchant store (swap for JPA in production).
 */
public class MerchantRepository {

    private final Map<String, Merchant> merchants = new HashMap<>();

    public void save(Merchant merchant) {
        merchants.put(merchant.getMerchantId(), merchant);
    }

    public Optional<Merchant> findById(String merchantId) {
        return Optional.ofNullable(merchants.get(merchantId));
    }
}
