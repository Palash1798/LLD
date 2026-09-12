package com.inventory.inventorymanagement.services;

import com.inventory.inventorymanagement.commands.FulfillCommand;
import com.inventory.inventorymanagement.commands.ReleaseCommand;
import com.inventory.inventorymanagement.commands.ReserveCommand;
import com.inventory.inventorymanagement.commands.StockInCommand;
import com.inventory.inventorymanagement.commands.StockMovementCommand;
import com.inventory.inventorymanagement.exceptions.ProductNotFoundException;
import com.inventory.inventorymanagement.models.InventoryItem;
import com.inventory.inventorymanagement.models.Product;
import com.inventory.inventorymanagement.models.StockMovement;
import com.inventory.inventorymanagement.observers.InventoryObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Step 7 — FACADE over warehouse stock operations.
 *
 * Owns:
 *   - product catalog (Map sku → Product)
 *   - inventory rows (Map warehouseId:sku → InventoryItem)
 *   - movement audit log
 *   - observer list for low-stock alerts
 *
 * Study order: read addStock → reserve → fulfill → release.
 */
public class InventoryService {

    private final Map<String, Product> products = new HashMap<>();
    private final Map<String, InventoryItem> inventory = new HashMap<>();
    private final List<StockMovement> movementLog = new ArrayList<>();
    private final List<InventoryObserver> observers = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Setup (used by CatalogFactory)
    // -------------------------------------------------------------------------

    public void registerProduct(Product product) {
        products.put(product.getSku(), product);
    }

    public void registerInventoryItem(InventoryItem item) {
        inventory.put(inventoryKey(item.getWarehouseId(), item.getProductId()), item);
    }

    public void registerObserver(InventoryObserver observer) {
        observers.add(observer);
    }

    // -------------------------------------------------------------------------
    // Feature 1: Restock (STOCK_IN)
    // -------------------------------------------------------------------------

    /**
     * Step 7a — Add stock when shipment arrives.
     * Creates InventoryItem row if this is the first restock for (warehouse, product).
     */
    public void addStock(String warehouseId, String productId, int quantity, String referenceId) {
        validateProductExists(productId);
        InventoryItem item = getOrCreateItem(warehouseId, productId);

        synchronized (item) {
            StockMovementCommand command = new StockInCommand(item, quantity, referenceId);
            executeAndLog(command);
        }
        notifyObservers(item);
    }

    /** Convenience overload for demos. */
    public void addStock(String warehouseId, String productId, int quantity) {
        addStock(warehouseId, productId, quantity, "SHIPMENT");
    }

    // -------------------------------------------------------------------------
    // Read: check availability
    // -------------------------------------------------------------------------

    public int getAvailable(String warehouseId, String productId) {
        validateProductExists(productId);
        InventoryItem item = inventory.get(inventoryKey(warehouseId, productId));
        if (item == null) {
            return 0;
        }
        return item.getAvailable();
    }

    public InventoryItem getInventoryItem(String warehouseId, String productId) {
        validateProductExists(productId);
        return inventory.get(inventoryKey(warehouseId, productId));
    }

    public Product getProduct(String sku) {
        Product product = products.get(sku);
        if (product == null) {
            throw new ProductNotFoundException(sku);
        }
        return product;
    }

    public List<StockMovement> getMovementLog() {
        return Collections.unmodifiableList(movementLog);
    }

    // -------------------------------------------------------------------------
    // Feature 2: Reserve (order created)
    // -------------------------------------------------------------------------

    /**
     * Step 7b — Hold stock for a pending order.
     * Called by OrderService during createOrder — oversell check happens here.
     */
    public void reserve(String warehouseId, String productId, int quantity, String orderId) {
        validateProductExists(productId);
        InventoryItem item = getOrCreateItem(warehouseId, productId);

        synchronized (item) {
            StockMovementCommand command = new ReserveCommand(item, quantity, orderId);
            executeAndLog(command);
        }
    }

    // -------------------------------------------------------------------------
    // Feature 3: Fulfill (order shipped)
    // -------------------------------------------------------------------------

    /**
     * Step 7c — Deduct on-hand and clear reservation when order ships.
     */
    public void fulfill(String warehouseId, String productId, int quantity, String orderId) {
        InventoryItem item = requireItem(warehouseId, productId);

        synchronized (item) {
            StockMovementCommand command = new FulfillCommand(item, quantity, orderId);
            executeAndLog(command);
        }
        notifyObservers(item);
    }

    // -------------------------------------------------------------------------
    // Cancel support: Release reservation
    // -------------------------------------------------------------------------

    /**
     * Step 7d — Order cancelled — release reserved qty back to available pool.
     */
    public void release(String warehouseId, String productId, int quantity, String orderId) {
        InventoryItem item = requireItem(warehouseId, productId);

        synchronized (item) {
            StockMovementCommand command = new ReleaseCommand(item, quantity, orderId);
            executeAndLog(command);
        }
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    private void executeAndLog(StockMovementCommand command) {
        StockMovement movement = command.execute();
        movementLog.add(movement);
        System.out.println("[Audit] " + movement);
    }

    private InventoryItem getOrCreateItem(String warehouseId, String productId) {
        String key = inventoryKey(warehouseId, productId);
        return inventory.computeIfAbsent(key, k -> new InventoryItem(warehouseId, productId));
    }

    private InventoryItem requireItem(String warehouseId, String productId) {
        validateProductExists(productId);
        InventoryItem item = inventory.get(inventoryKey(warehouseId, productId));
        if (item == null) {
            throw new IllegalStateException("No inventory row for " + warehouseId + "/" + productId);
        }
        return item;
    }

    private void validateProductExists(String productId) {
        if (!products.containsKey(productId)) {
            throw new ProductNotFoundException(productId);
        }
    }

    private void notifyObservers(InventoryItem item) {
        for (InventoryObserver observer : observers) {
            observer.onStockChanged(item);
        }
    }

    /** Composite key for in-memory map (production: DB composite PK). */
    public static String inventoryKey(String warehouseId, String productId) {
        return warehouseId + ":" + productId;
    }

    public void displayStock(String warehouseId) {
        System.out.println("--- Stock at " + warehouseId + " ---");
        inventory.values().stream()
                .filter(item -> item.getWarehouseId().equals(warehouseId))
                .forEach(item -> {
                    Product p = products.get(item.getProductId());
                    System.out.println("  " + item.getProductId()
                            + " (" + (p != null ? p.getName() : "?") + ")"
                            + " | onHand=" + item.getQuantityOnHand()
                            + " reserved=" + item.getQuantityReserved()
                            + " available=" + item.getAvailable());
                });
    }
}
