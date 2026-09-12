package com.paymentgateway.paymentgateway.controller;

import com.paymentgateway.paymentgateway.dto.CreateOrderRequest;
import com.paymentgateway.paymentgateway.dto.CreateOrderResponse;
import com.paymentgateway.paymentgateway.dto.PaymentStatusResponse;
import com.paymentgateway.paymentgateway.dto.ProcessPaymentRequest;
import com.paymentgateway.paymentgateway.models.PaymentOrder;
import com.paymentgateway.paymentgateway.services.PaymentService;

import java.util.List;

/**
 * Step 9 — Thin controller layer (CLI today, @RestController tomorrow).
 *
 * Same role as AtmController / MatchController — no business logic here.
 */
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /** MVP Feature 1 */
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        return paymentService.createOrder(request);
    }

    /** MVP Feature 2 */
    public PaymentStatusResponse processPayment(ProcessPaymentRequest request) {
        return paymentService.processPayment(request);
    }

    /** MVP Feature 3 */
    public PaymentStatusResponse getStatus(String orderId) {
        return paymentService.getStatus(orderId);
    }

    /** Extension */
    public PaymentStatusResponse refund(String orderId) {
        return paymentService.refund(orderId);
    }

    public List<PaymentOrder> getOrderHistory(String merchantId, String apiKey) {
        return paymentService.getOrderHistory(merchantId, apiKey);
    }

    public void displayOrderHistory(String merchantId, String apiKey) {
        List<PaymentOrder> orders = getOrderHistory(merchantId, apiKey);
        System.out.println("--- Order history for " + merchantId + " ---");
        if (orders.isEmpty()) {
            System.out.println("(no orders)");
            return;
        }
        for (PaymentOrder order : orders) {
            System.out.println("  " + order);
        }
    }
}
