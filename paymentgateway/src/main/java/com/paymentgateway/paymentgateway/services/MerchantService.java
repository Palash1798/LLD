package com.paymentgateway.paymentgateway.services;

import com.paymentgateway.paymentgateway.exceptions.InvalidMerchantException;
import com.paymentgateway.paymentgateway.models.Merchant;
import com.paymentgateway.paymentgateway.repositories.MerchantRepository;

/**
 * Step 7 — Validates merchant identity before any order operation.
 *
 * Real system: apiKey hashed, rate limits, merchant status ACTIVE check.
 */
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public void registerMerchant(Merchant merchant) {
        merchantRepository.save(merchant);
        System.out.println("[MerchantService] Registered " + merchant);
    }

    /**
     * Step 7a: Called at start of createOrder — reject unknown or wrong apiKey.
     */
    public Merchant validateMerchant(String merchantId, String apiKey) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new InvalidMerchantException("Unknown merchant: " + merchantId));

        if (!merchant.getApiKey().equals(apiKey)) {
            throw new InvalidMerchantException("Invalid API key for merchant: " + merchantId);
        }

        return merchant;
    }
}
