package com.inventory.inventorymanagement.factories;

import com.inventory.inventorymanagement.models.Warehouse;
import com.inventory.inventorymanagement.services.InventoryService;
import com.inventory.inventorymanagement.services.OrderService;

/**
 * Step 9 — Holds all wired objects returned by CatalogFactory.
 * Makes demo setup one-liner: CatalogContext ctx = CatalogFactory.create();
 */
public class CatalogContext {

    private final Warehouse warehouse;
    private final InventoryService inventoryService;
    private final OrderService orderService;

    public CatalogContext(Warehouse warehouse,
                          InventoryService inventoryService,
                          OrderService orderService) {
        this.warehouse = warehouse;
        this.inventoryService = inventoryService;
        this.orderService = orderService;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public String getWarehouseId() {
        return warehouse.getId();
    }
}
