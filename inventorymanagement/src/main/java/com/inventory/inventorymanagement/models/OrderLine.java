package com.inventory.inventorymanagement.models;

/**
 * Step 2 — One line on a customer order: product SKU + quantity.
 */
public class OrderLine {

    private final String productId;
    private final int quantity;

    public OrderLine(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return productId + " x" + quantity;
    }
}
