package com.paymentgateway.paymentgateway.factories;

import com.paymentgateway.paymentgateway.controller.PaymentController;
import com.paymentgateway.paymentgateway.models.Merchant;
import com.paymentgateway.paymentgateway.repositories.MerchantRepository;
import com.paymentgateway.paymentgateway.repositories.OrderRepository;
import com.paymentgateway.paymentgateway.repositories.TransactionRepository;
import com.paymentgateway.paymentgateway.services.MerchantService;
import com.paymentgateway.paymentgateway.services.PaymentService;

/**
 * Step 6 — Wires all components for demo / interview setup.
 *
 * Like ATM AccountFactory — seeds merchants and returns a ready PaymentController.
 */
public final class DemoDataFactory {

    /** Demo merchant credentials — use these in AtmDemo-style scripts. */
    public static final String MERCHANT_ID = "merchant_shopify_001";
    public static final String API_KEY = "sk_test_abc123";
    public static final String MERCHANT_NAME = "Demo E-Commerce Store";

    private DemoDataFactory() {
    }

    public static PaymentController createPaymentController() {
        // Step 6a: create repositories (in-memory)
        MerchantRepository merchantRepository = new MerchantRepository();
        OrderRepository orderRepository = new OrderRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        // Step 6b: create services
        MerchantService merchantService = new MerchantService(merchantRepository);
        PaymentStrategyFactory strategyFactory = new PaymentStrategyFactory();
        PaymentService paymentService = new PaymentService(
                orderRepository,
                transactionRepository,
                merchantService,
                strategyFactory
        );

        // Step 6c: seed demo merchant
        merchantService.registerMerchant(new Merchant(
                MERCHANT_ID,
                MERCHANT_NAME,
                API_KEY,
                "https://demo-store.example/webhook/payment"
        ));

        // Step 6d: return thin controller
        return new PaymentController(paymentService);
    }
}
