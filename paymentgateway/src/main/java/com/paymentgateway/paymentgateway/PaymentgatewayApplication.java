package com.paymentgateway.paymentgateway;

import com.paymentgateway.paymentgateway.controller.PaymentController;
import com.paymentgateway.paymentgateway.dto.CreateOrderRequest;
import com.paymentgateway.paymentgateway.dto.CreateOrderResponse;
import com.paymentgateway.paymentgateway.dto.PaymentStatusResponse;
import com.paymentgateway.paymentgateway.dto.ProcessPaymentRequest;
import com.paymentgateway.paymentgateway.enums.PaymentMethod;
import com.paymentgateway.paymentgateway.factories.DemoDataFactory;
import com.paymentgateway.paymentgateway.models.PaymentInstrumentDetails;

/**
 * Entry point — scripted study demo (like AtmApplication).
 *
 * Walks through the 3 MVP features from LLD_PAYMENT_GATEWAY.md:
 *   1) Create payment order (with idempotency)
 *   2) Process payment via Card / UPI (Strategy pattern)
 *   3) Get payment status + transaction history
 *
 * IDE: run this class (or demo.PaymentGatewayDemo for interactive menu).
 *
 * Note: Spring Boot web context is optional — this main() runs standalone for LLD practice.
 */
public class PaymentgatewayApplication {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  PAYMENT GATEWAY LLD — STUDY DEMO");
        System.out.println("  Patterns: Strategy + Factory + Order State");
        System.out.println("========================================\n");

        printDemoCredentials();

        // Step 0: wire all components via factory
        PaymentController controller = DemoDataFactory.createPaymentController();

        // ------------------------------------------------------------------
        // FEATURE 1+2+3: Happy path — UPI payment
        // ------------------------------------------------------------------
        section("FEATURE 1+2+3 — Happy path (UPI)");
        safe(() -> {
            CreateOrderResponse order = controller.createOrder(new CreateOrderRequest(
                    DemoDataFactory.MERCHANT_ID,
                    DemoDataFactory.API_KEY,
                    50_000,           // ₹500.00 in paise
                    "idem-happy-upi-1",
                    "SHOP-ORDER-101"
            ));
            System.out.println("Created: " + order);

            PaymentStatusResponse paid = controller.processPayment(new ProcessPaymentRequest(
                    order.getOrderId(),
                    PaymentMethod.UPI,
                    PaymentInstrumentDetails.upi("customer@paytm")
            ));
            System.out.println("After payment: " + paid);

            PaymentStatusResponse status = controller.getStatus(order.getOrderId());
            System.out.println("Status poll: " + status);
            printTransactionHistory(status);
        });

        // ------------------------------------------------------------------
        // FEATURE 1: Idempotent createOrder
        // ------------------------------------------------------------------
        section("FEATURE 1 — Idempotent createOrder (same key → same orderId)");
        safe(() -> {
            CreateOrderResponse first = controller.createOrder(new CreateOrderRequest(
                    DemoDataFactory.MERCHANT_ID,
                    DemoDataFactory.API_KEY,
                    10_000,
                    "idem-duplicate-test",
                    "SHOP-ORDER-202"
            ));
            CreateOrderResponse second = controller.createOrder(new CreateOrderRequest(
                    DemoDataFactory.MERCHANT_ID,
                    DemoDataFactory.API_KEY,
                    10_000,
                    "idem-duplicate-test",
                    "SHOP-ORDER-202"
            ));
            System.out.println("First orderId:  " + first.getOrderId());
            System.out.println("Second orderId: " + second.getOrderId());
            System.out.println("Same order? " + first.getOrderId().equals(second.getOrderId()));
        });

        // ------------------------------------------------------------------
        // FAIL PATH: Card declined, then UPI retry succeeds
        // ------------------------------------------------------------------
        section("FAIL PATH — Card declined, UPI retry succeeds");
        final String[] retryOrderId = { null };
        safe(() -> {
            CreateOrderResponse order = controller.createOrder(new CreateOrderRequest(
                    DemoDataFactory.MERCHANT_ID,
                    DemoDataFactory.API_KEY,
                    12_000,
                    "idem-retry-1",
                    "SHOP-ORDER-303"
            ));
            retryOrderId[0] = order.getOrderId();

            PaymentStatusResponse failed = controller.processPayment(new ProcessPaymentRequest(
                    order.getOrderId(),
                    PaymentMethod.CARD,
                    PaymentInstrumentDetails.failingCard("4111111111111111", "123")
            ));
            System.out.println("After failed CARD: " + failed);

            PaymentStatusResponse success = controller.processPayment(new ProcessPaymentRequest(
                    order.getOrderId(),
                    PaymentMethod.UPI,
                    PaymentInstrumentDetails.upi("retry@upi")
            ));
            System.out.println("After UPI retry: " + success);
            printTransactionHistory(success);
        });

        // ------------------------------------------------------------------
        // FAIL PATH: Pay already PAID order
        // ------------------------------------------------------------------
        section("FAIL PATH — Pay already PAID order");
        safe(() -> controller.processPayment(new ProcessPaymentRequest(
                retryOrderId[0],
                PaymentMethod.UPI,
                PaymentInstrumentDetails.upi("someone@upi")
        )));

        // ------------------------------------------------------------------
        // FAIL PATH: Invalid merchant API key
        // ------------------------------------------------------------------
        section("FAIL PATH — Invalid API key");
        safe(() -> controller.createOrder(new CreateOrderRequest(
                DemoDataFactory.MERCHANT_ID,
                "wrong-key",
                5_000,
                "idem-bad-key",
                "SHOP-ORDER-404"
        )));

        // ------------------------------------------------------------------
        // EXTENSION: Refund
        // ------------------------------------------------------------------
        section("EXTENSION — Refund PAID order");
        safe(() -> {
            CreateOrderResponse order = controller.createOrder(new CreateOrderRequest(
                    DemoDataFactory.MERCHANT_ID,
                    DemoDataFactory.API_KEY,
                    3_000,
                    "idem-refund-1",
                    "SHOP-ORDER-505"
            ));
            controller.processPayment(new ProcessPaymentRequest(
                    order.getOrderId(),
                    PaymentMethod.CARD,
                    PaymentInstrumentDetails.card("5555555555554444", "999")
            ));
            PaymentStatusResponse refunded = controller.refund(order.getOrderId());
            System.out.println("After refund: " + refunded);
        });

        // ------------------------------------------------------------------
        // Order history for merchant
        // ------------------------------------------------------------------
        section("Merchant order history");
        controller.displayOrderHistory(DemoDataFactory.MERCHANT_ID, DemoDataFactory.API_KEY);

        System.out.println("\nDemo complete. Re-read services/PaymentService.java — that is the heart of this LLD.");
        System.out.println("For hands-on practice, run: com.paymentgateway.paymentgateway.demo.PaymentGatewayDemo");
    }

    private static void printDemoCredentials() {
        System.out.println("Demo merchant (from DemoDataFactory):");
        System.out.println("  merchantId = " + DemoDataFactory.MERCHANT_ID);
        System.out.println("  apiKey     = " + DemoDataFactory.API_KEY);
        System.out.println("  Amounts are in paise (50000 = ₹500.00)\n");
    }

    private static void printTransactionHistory(PaymentStatusResponse status) {
        System.out.println("Transaction attempts:");
        for (String line : status.getTransactionHistory()) {
            System.out.println("  " + line);
        }
    }

    private static void section(String title) {
        System.out.println("\n>>> " + title);
        System.out.println("------------------------------------------------");
    }

    private static void safe(Runnable action) {
        try {
            action.run();
        } catch (RuntimeException ex) {
            System.out.println("!! " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }
}
