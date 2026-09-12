package com.inventory.inventorymanagement.services;

import com.inventory.inventorymanagement.enums.OrderStatus;
import com.inventory.inventorymanagement.exceptions.InvalidOrderStateException;
import com.inventory.inventorymanagement.exceptions.OrderNotFoundException;
import com.inventory.inventorymanagement.models.Order;
import com.inventory.inventorymanagement.models.OrderLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Step 8 — Orchestrates order lifecycle and delegates stock ops to InventoryService.
 *
 * createOrder  → reserve stock for each line (rollback if any line fails)
 * fulfillOrder → fulfill each line, mark FULFILLED
 * cancelOrder  → release each line, mark CANCELLED
 */
public class OrderService {

    private final InventoryService inventoryService;
    private final Map<String, Order> orders = new HashMap<>();
    private final AtomicInteger orderCounter = new AtomicInteger(1);

    public OrderService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // -------------------------------------------------------------------------
    // Feature 2: Create order + reserve stock
    // -------------------------------------------------------------------------

    /**
     * Step 8a — Create order and reserve inventory atomically (all-or-nothing per order).
     *
     * If line 2 fails (insufficient stock), we release line 1's reservation (rollback).
     */
    public Order createOrder(String warehouseId, List<OrderLine> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one line");
        }

        String orderId = "ORD-" + orderCounter.getAndIncrement();
        Order order = new Order(orderId, warehouseId, lines);

        List<OrderLine> reservedLines = new ArrayList<>();
        try {
            for (OrderLine line : lines) {
                inventoryService.reserve(warehouseId, line.getProductId(), line.getQuantity(), orderId);
                reservedLines.add(line);
            }
        } catch (RuntimeException ex) {
            // Rollback: release anything we already reserved for this failed order
            rollbackReservations(warehouseId, orderId, reservedLines);
            throw ex;
        }

        orders.put(orderId, order);
        System.out.println("[OrderService] Created " + order);
        return order;
    }

    // -------------------------------------------------------------------------
    // Feature 3: Fulfill order
    // -------------------------------------------------------------------------

    /**
     * Step 8b — Ship order: deduct on-hand, clear reservations.
     */
    public void fulfillOrder(String orderId) {
        Order order = requirePendingOrder(orderId, "fulfill");

        for (OrderLine line : order.getLines()) {
            inventoryService.fulfill(
                    order.getWarehouseId(),
                    line.getProductId(),
                    line.getQuantity(),
                    orderId
            );
        }
        order.setStatus(OrderStatus.FULFILLED);
        System.out.println("[OrderService] Fulfilled " + orderId);
    }

    // -------------------------------------------------------------------------
    // Cancel order
    // -------------------------------------------------------------------------

    /**
     * Step 8c — Cancel pending order: release reservations only (onHand unchanged).
     */
    public void cancelOrder(String orderId) {
        Order order = requirePendingOrder(orderId, "cancel");

        for (OrderLine line : order.getLines()) {
            inventoryService.release(
                    order.getWarehouseId(),
                    line.getProductId(),
                    line.getQuantity(),
                    orderId
            );
        }
        order.setStatus(OrderStatus.CANCELLED);
        System.out.println("[OrderService] Cancelled " + orderId);
    }

    // -------------------------------------------------------------------------
    // Read
    // -------------------------------------------------------------------------

    public Order getOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return order;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Order requirePendingOrder(String orderId, String operation) {
        Order order = getOrder(orderId);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStateException(orderId, order.getStatus(), operation);
        }
        return order;
    }

    private void rollbackReservations(String warehouseId, String orderId, List<OrderLine> reservedLines) {
        for (OrderLine line : reservedLines) {
            inventoryService.release(warehouseId, line.getProductId(), line.getQuantity(), orderId);
        }
        System.out.println("[OrderService] Rolled back reservations for failed order " + orderId);
    }
}
