package com.inventory.inventorymanagement.models;

/**
 * Step 2 — Product catalog entry (SKU is the unique business key).
 *
 * reorderThreshold: when onHand drops to this level or below,
 * LowStockNotifier (Observer) fires an alert.
 */
public class Product {

    private final String sku;
    private final String name;
    private final int reorderThreshold;

    public Product(String sku, String name, int reorderThreshold) {
        this.sku = sku;
        this.name = name;
        this.reorderThreshold = reorderThreshold;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public int getReorderThreshold() {
        return reorderThreshold;
    }

    @Override
    public String toString() {
        return "Product{" + sku + ", name='" + name + "', reorderAt=" + reorderThreshold + "}";
    }
}
