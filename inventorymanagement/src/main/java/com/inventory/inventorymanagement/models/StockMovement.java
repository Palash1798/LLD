package com.inventory.inventorymanagement.models;

import com.inventory.inventorymanagement.enums.MovementType;

/**
 * Step 4 — Immutable audit record for every stock change (Command pattern output).
 *
 * In production this row goes to stock_movements table — append-only log.
 */
public class StockMovement {

    private final MovementType type;
    private final String warehouseId;
    private final String productId;
    private final int quantity;
    private final String referenceId;
    private final long timestamp;

    public StockMovement(MovementType type, String warehouseId, String productId,
                         int quantity, String referenceId) {
        this.type = type;
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.quantity = quantity;
        this.referenceId = referenceId;
        this.timestamp = System.currentTimeMillis();
    }

    public MovementType getType() {
        return type;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "StockMovement{" + type + ", " + warehouseId + "/" + productId
                + ", qty=" + quantity + ", ref=" + referenceId + "}";
    }
}
