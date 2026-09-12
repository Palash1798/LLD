package com.inventory.inventorymanagement.controller;

import com.inventory.inventorymanagement.models.Order;
import com.inventory.inventorymanagement.models.OrderLine;
import com.inventory.inventorymanagement.services.InventoryService;
import com.inventory.inventorymanagement.services.OrderService;

import java.util.List;

/**
 * Step 10 — Thin controller (same idea as AtmController / VendingController).
 *
 * Keeps CLI/API free of business rules — services own the logic.
 */
public class InventoryController {

    private final InventoryService inventoryService;
    private final OrderService orderService;

    public InventoryController(InventoryService inventoryService, OrderService orderService) {
        this.inventoryService = inventoryService;
        this.orderService = orderService;
    }

    /** Feature 1: restock a product at a warehouse. */
    public void addStock(String warehouseId, String productId, int quantity) {
        inventoryService.addStock(warehouseId, productId, quantity);
    }

    /** Read: how many units can still be ordered? */
    public int getAvailable(String warehouseId, String productId) {
        return inventoryService.getAvailable(warehouseId, productId);
    }

    /** Feature 2: create order and reserve stock. */
    public Order createOrder(String warehouseId, List<OrderLine> lines) {
        return orderService.createOrder(warehouseId, lines);
    }

    /** Feature 3: ship order — deduct on-hand. */
    public void fulfillOrder(String orderId) {
        orderService.fulfillOrder(orderId);
    }

    /** Cancel pending order — release reservation. */
    public void cancelOrder(String orderId) {
        orderService.cancelOrder(orderId);
    }

    public Order getOrder(String orderId) {
        return orderService.getOrder(orderId);
    }

    public void displayStock(String warehouseId) {
        inventoryService.displayStock(warehouseId);
    }

    public void displayMovementLog() {
        System.out.println("--- Stock movement audit log ---");
        inventoryService.getMovementLog().forEach(m -> System.out.println("  " + m));
    }
}
