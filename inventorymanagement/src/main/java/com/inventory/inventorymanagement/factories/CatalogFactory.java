package com.inventory.inventorymanagement.factories;

import com.inventory.inventorymanagement.models.Product;
import com.inventory.inventorymanagement.models.Warehouse;
import com.inventory.inventorymanagement.observers.LowStockNotifier;
import com.inventory.inventorymanagement.services.InventoryService;
import com.inventory.inventorymanagement.services.OrderService;

/**
 * Step 9 — Seeds demo catalog, warehouse, and services for study walkthroughs.
 *
 * Demo SKUs (memorize for quick interviews):
 *   SKU-001  Wireless Mouse   reorder threshold 10
 *   SKU-002  USB-C Hub        reorder threshold 5
 *   SKU-003  Laptop Stand     reorder threshold 8
 *
 * Warehouse: WH-001 (Mumbai DC)
 */
public final class CatalogFactory {

    private CatalogFactory() {
        // utility class
    }

    public static CatalogContext create() {
        // Step 1: warehouse
        Warehouse warehouse = new Warehouse("WH-001", "Mumbai DC", "Mumbai, India");

        // Step 2: inventory service + products
        InventoryService inventoryService = new InventoryService();

        Product mouse = new Product("SKU-001", "Wireless Mouse", 10);
        Product hub = new Product("SKU-002", "USB-C Hub", 5);
        Product stand = new Product("SKU-003", "Laptop Stand", 8);

        inventoryService.registerProduct(mouse);
        inventoryService.registerProduct(hub);
        inventoryService.registerProduct(stand);

        // Step 3: low-stock observer (fires after restock/fulfill when onHand <= threshold)
        inventoryService.registerObserver(new LowStockNotifier(
                java.util.Map.of(
                        mouse.getSku(), mouse,
                        hub.getSku(), hub,
                        stand.getSku(), stand
                )
        ));

        // Step 4: order service depends on inventory
        OrderService orderService = new OrderService(inventoryService);

        return new CatalogContext(warehouse, inventoryService, orderService);
    }
}
