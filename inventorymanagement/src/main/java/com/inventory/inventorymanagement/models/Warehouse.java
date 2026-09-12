package com.inventory.inventorymanagement.models;

/**
 * Step 2 — Physical location that holds inventory.
 *
 * MVP uses a single warehouse (WH-001). Multi-warehouse is an extension:
 * same InventoryItem model, keyed by (warehouseId + productId).
 */
public class Warehouse {

    private final String id;
    private final String name;
    private final String location;

    public Warehouse(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Warehouse{" + id + ", name='" + name + "', location='" + location + "'}";
    }
}
