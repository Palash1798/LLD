package com.paymentgateway.paymentgateway.services;

import com.paymentgateway.paymentgateway.dto.CreateOrderRequest;
import com.paymentgateway.paymentgateway.dto.CreateOrderResponse;
import com.paymentgateway.paymentgateway.dto.PaymentStatusResponse;
import com.paymentgateway.paymentgateway.dto.ProcessPaymentRequest;
import com.paymentgateway.paymentgateway.enums.OrderStatus;
import com.paymentgateway.paymentgateway.enums.TransactionStatus;
import com.paymentgateway.paymentgateway.exceptions.InvalidOrderStateException;
import com.paymentgateway.paymentgateway.exceptions.OrderNotFoundException;
import com.paymentgateway.paymentgateway.factories.PaymentStrategyFactory;
import com.paymentgateway.paymentgateway.models.PaymentOrder;
import com.paymentgateway.paymentgateway.models.PaymentResult;
import com.paymentgateway.paymentgateway.models.PaymentTransaction;
import com.paymentgateway.paymentgateway.repositories.OrderRepository;
import com.paymentgateway.paymentgateway.repositories.TransactionRepository;
import com.paymentgateway.paymentgateway.strategies.PaymentStrategy;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Step 8 — ORCHESTRATOR (heart of Payment Gateway LLD).
 *
 * Responsibilities:
 *   1. createOrder  — idempotent order creation
 *   2. processPayment — validate order state → Strategy.pay → update order/txn
 *   3. getStatus    — read order + transaction history
 *   4. refund       — extension on PAID orders
 *
 * Does NOT contain if-else per payment method — delegates to PaymentStrategyFactory.
 */
public class PaymentService {

    /** Orders unpaid after this window move to EXPIRED. */
    private static final long ORDER_TTL_MINUTES = 15;

    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;
    private final MerchantService merchantService;
    private final PaymentStrategyFactory strategyFactory;

    public PaymentService(OrderRepository orderRepository,
                          TransactionRepository transactionRepository,
                          MerchantService merchantService,
                          PaymentStrategyFactory strategyFactory) {
        this.orderRepository = orderRepository;
        this.transactionRepository = transactionRepository;
        this.merchantService = merchantService;
        this.strategyFactory = strategyFactory;
    }

    // =========================================================================
    // FEATURE 1: CREATE PAYMENT ORDER
    // =========================================================================

    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        // Step 8.1 — Authenticate merchant
        merchantService.validateMerchant(request.getMerchantId(), request.getApiKey());

        // Step 8.2 — Validate amount (integer paise/cents — never float)
        if (request.getAmountCents() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (request.getIdempotencyKey() == null || request.getIdempotencyKey().isBlank()) {
            throw new IllegalArgumentException("Idempotency key is required");
        }

        // Step 8.3 — Idempotency: same key → return existing order (no duplicate charge intent)
        var existing = orderRepository.findByIdempotencyKey(
                request.getMerchantId(),
                request.getIdempotencyKey()
        );
        if (existing.isPresent()) {
            PaymentOrder order = existing.get();
            System.out.println("[PaymentService] Idempotent createOrder — returning existing " + order.getOrderId());
            return toCreateOrderResponse(order);
        }

        // Step 8.4 — Create new order in CREATED state
        long now = System.currentTimeMillis();
        PaymentOrder order = new PaymentOrder(
                UUID.randomUUID().toString(),
                request.getMerchantId(),
                request.getAmountCents(),
                "INR",
                OrderStatus.CREATED,
                request.getIdempotencyKey(),
                request.getMerchantOrderRef(),
                now,
                now + TimeUnit.MINUTES.toMillis(ORDER_TTL_MINUTES)
        );

        orderRepository.save(order);
        System.out.println("[PaymentService] Created order " + order);

        return toCreateOrderResponse(order);
    }

    // =========================================================================
    // FEATURE 2: PROCESS PAYMENT (Strategy pattern invoked here)
    // =========================================================================

    public PaymentStatusResponse processPayment(ProcessPaymentRequest request) {
        // Step 8.5 — Load order
        PaymentOrder order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(request.getOrderId()));

        // Step 8.6 — Enforce order state machine
        if (order.getStatus() == OrderStatus.PAID) {
            throw new InvalidOrderStateException(order.getOrderId(), order.getStatus(), "pay again");
        }
        if (order.getStatus() == OrderStatus.REFUNDED) {
            throw new InvalidOrderStateException(order.getOrderId(), order.getStatus(), "pay");
        }

        // Step 8.7 — Check expiry
        long now = System.currentTimeMillis();
        if (order.isExpired(now)) {
            order.setStatus(OrderStatus.EXPIRED);
            orderRepository.save(order);
            throw new InvalidOrderStateException(order.getOrderId(), OrderStatus.EXPIRED, "pay expired order");
        }

        // Step 8.8 — Create transaction record (audit trail for this attempt)
        PaymentTransaction txn = new PaymentTransaction(
                UUID.randomUUID().toString(),
                order.getOrderId(),
                request.getMethod(),
                TransactionStatus.INITIATED,
                order.getAmountCents(),
                now
        );
        txn.setStatus(TransactionStatus.PROCESSING);
        transactionRepository.save(txn);

        // Step 8.9 — STRATEGY PATTERN: lookup processor by payment method
        PaymentStrategy strategy = strategyFactory.getStrategy(request.getMethod());
        PaymentResult result = strategy.pay(order.getAmountCents(), request.getInstrumentDetails());

        // Step 8.10 — Update transaction + order based on processor result
        if (result.isSuccess()) {
            txn.setStatus(TransactionStatus.SUCCESS);
            txn.setProcessorRef(result.getProcessorRef());
            order.setStatus(OrderStatus.PAID);
            notifyMerchantWebhook(order, txn);
        } else {
            txn.setStatus(TransactionStatus.FAILED);
            txn.setFailureReason(result.getMessage());
            // Order stays CREATED — customer can retry with another method
            System.out.println("[PaymentService] Payment failed — order remains CREATED for retry");
        }

        // Step 8.11 — Persist updates (in prod: single DB transaction)
        orderRepository.save(order);

        System.out.println("[PaymentService] processPayment complete — order=" + order.getStatus()
                + ", txn=" + txn.getStatus());

        return buildStatusResponse(order);
    }

    // =========================================================================
    // FEATURE 3: GET PAYMENT STATUS + HISTORY
    // =========================================================================

    public PaymentStatusResponse getStatus(String orderId) {
        PaymentOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        // Lazy expiry check on read
        if (order.getStatus() == OrderStatus.CREATED && order.isExpired(System.currentTimeMillis())) {
            order.setStatus(OrderStatus.EXPIRED);
            orderRepository.save(order);
        }

        return buildStatusResponse(order);
    }

    public List<PaymentOrder> getOrderHistory(String merchantId, String apiKey) {
        merchantService.validateMerchant(merchantId, apiKey);
        return orderRepository.findByMerchantId(merchantId);
    }

    // =========================================================================
    // EXTENSION: REFUND (bonus — fully wired for study)
    // =========================================================================

    public PaymentStatusResponse refund(String orderId) {
        PaymentOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (order.getStatus() != OrderStatus.PAID) {
            throw new InvalidOrderStateException(orderId, order.getStatus(), "refund");
        }

        PaymentTransaction originalTxn = transactionRepository.findLatestSuccessfulByOrderId(orderId)
                .orElseThrow(() -> new IllegalStateException("No successful transaction to refund"));

        PaymentStrategy strategy = strategyFactory.getStrategy(originalTxn.getMethod());
        PaymentResult result = strategy.refund(order.getAmountCents(), originalTxn.getProcessorRef());

        if (result.isSuccess()) {
            order.setStatus(OrderStatus.REFUNDED);
            orderRepository.save(order);
            System.out.println("[PaymentService] Refund successful for order " + orderId);
        } else {
            throw new IllegalStateException("Refund failed: " + result.getMessage());
        }

        return buildStatusResponse(order);
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    private PaymentStatusResponse buildStatusResponse(PaymentOrder order) {
        List<PaymentTransaction> txns = transactionRepository.findByOrderId(order.getOrderId());
        return PaymentStatusResponse.from(order, txns);
    }

    private CreateOrderResponse toCreateOrderResponse(PaymentOrder order) {
        return new CreateOrderResponse(
                order.getOrderId(),
                order.getStatus(),
                order.getAmountCents(),
                order.getCurrency(),
                order.getExpiresAt()
        );
    }

    /**
     * Step 8.12 — Extension hook: Observer/webhook would live here.
     * Demo prints to console instead of HTTP POST to merchant.webhookUrl.
     */
    private void notifyMerchantWebhook(PaymentOrder order, PaymentTransaction txn) {
        System.out.println("[Webhook] POST merchant webhook — orderId=" + order.getOrderId()
                + ", status=PAID, txnRef=" + txn.getProcessorRef()
                + ", amountCents=" + order.getAmountCents());
    }
}
