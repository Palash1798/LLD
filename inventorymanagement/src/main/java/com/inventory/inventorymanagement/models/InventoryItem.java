package com.inventory.inventorymanagement.models;

import com.inventory.inventorymanagement.exceptions.InsufficientStockException;

/**
 * Step 3 — HEART of inventory LLD: stock accounting for one (warehouse, product) pair.
 *
 * Three numbers to memorize for interviews:
 *   quantityOnHand     = physical units in the warehouse
 *   quantityReserved   = units promised to pending orders
 *   available          = onHand - reserved  (what new orders can claim)
 *
 * Invariant: quantityOnHand >= quantityReserved (never negative available).
 *
 * Thread safety: methods are synchronized so two concurrent orders
 * cannot both reserve the last unit.
 */
public class InventoryItem {

    private final String warehouseId;
    private final String productId;
    private int quantityOnHand;
    private int quantityReserved;

    public InventoryItem(String warehouseId, String productId) {
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.quantityOnHand = 0;
        this.quantityReserved = 0;
    }

    public InventoryItem(String warehouseId, String productId, int quantityOnHand) {
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.quantityOnHand = quantityOnHand;
        this.quantityReserved = 0;
    }

    // -------------------------------------------------------------------------
    // Read helpers
    // -------------------------------------------------------------------------

    public String getWarehouseId() {
        return warehouseId;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantityOnHand() {
        return quantityOnHand;
    }

    public int getQuantityReserved() {
        return quantityReserved;
    }

    /**
     * Available = onHand - reserved.
     * This is what we check BEFORE accepting a new order.
     */
    public synchronized int getAvailable() {
        return quantityOnHand - quantityReserved;
    }

    // -------------------------------------------------------------------------
    // Stock mutations (called only via Command objects in InventoryService)
    // -------------------------------------------------------------------------

    /** Step 3a — Restock: shipment arrived at warehouse. */
    public synchronized void addOnHand(int quantity) {
        validatePositive(quantity);
        quantityOnHand += quantity;
        System.out.println("[InventoryItem] STOCK_IN " + productId
                + " +" + quantity + " → onHand=" + quantityOnHand
                + ", reserved=" + quantityReserved
                + ", available=" + getAvailable());
    }

    /**
     * Step 3b — Reserve: order created, hold stock without changing onHand.
     * Fails if available < quantity → prevents overselling.
     */
    public synchronized void reserve(int quantity) {
        validatePositive(quantity);
        int available = getAvailable();
        if (available < quantity) {
            throw new InsufficientStockException(productId, available, quantity);
        }
        quantityReserved += quantity;
        System.out.println("[InventoryItem] RESERVE " + productId
                + " " + quantity + " → onHand=" + quantityOnHand
                + ", reserved=" + quantityReserved
                + ", available=" + getAvailable());
    }

    /**
     * Step 3c — Release: order cancelled, give reserved stock back to pool.
     */
    public synchronized void release(int quantity) {
        validatePositive(quantity);
        if (quantityReserved < quantity) {
            throw new IllegalStateException("Cannot release " + quantity
                    + " — only " + quantityReserved + " reserved for " + productId);
        }
        quantityReserved -= quantity;
        System.out.println("[InventoryItem] RELEASE " + productId
                + " " + quantity + " → onHand=" + quantityOnHand
                + ", reserved=" + quantityReserved
                + ", available=" + getAvailable());
    }

    /**
     * Step 3d — Fulfill: order shipped — deduct BOTH onHand and reserved.
     * Only operation that touches both counters.
     */
    public synchronized void fulfill(int quantity) {
        validatePositive(quantity);
        if (quantityReserved < quantity) {
            throw new IllegalStateException("Cannot fulfill " + quantity
                    + " — only " + quantityReserved + " reserved for " + productId);
        }
        quantityOnHand -= quantity;
        quantityReserved -= quantity;
        System.out.println("[InventoryItem] FULFILL " + productId
                + " " + quantity + " → onHand=" + quantityOnHand
                + ", reserved=" + quantityReserved
                + ", available=" + getAvailable());
    }

    private void validatePositive(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive, got: " + quantity);
        }
    }

    @Override
    public String toString() {
        return "InventoryItem{" + warehouseId + "/" + productId
                + ", onHand=" + quantityOnHand
                + ", reserved=" + quantityReserved
                + ", available=" + getAvailable() + "}";
    }
}
