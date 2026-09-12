package com.inventory.inventorymanagement;

import com.inventory.inventorymanagement.controller.InventoryController;
import com.inventory.inventorymanagement.factories.CatalogContext;
import com.inventory.inventorymanagement.factories.CatalogFactory;
import com.inventory.inventorymanagement.models.Order;
import com.inventory.inventorymanagement.models.OrderLine;

import java.util.List;

/**
 * Entry point — scripted study demo (no Spring web needed).
 *
 * Walks through the 3 MVP features from LLD_INVENTORY_MANAGEMENT.md:
 *   1) Add product + restock
 *   2) Create order + reserve stock
 *   3) Fulfill order (deduct on-hand)
 *
 * IDE: run this class (or InventoryDemo for interactive menu).
 *
 * Study order: models/InventoryItem → services/InventoryService → commands/* → observers/*
 */
public class InventorymanagementApplication {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  INVENTORY MANAGEMENT LLD — STUDY DEMO");
        System.out.println("  Patterns: Command (movements) + Observer (low stock)");
        System.out.println("========================================\n");

        // Step 0: wire objects (like interview setup)
        CatalogContext ctx = CatalogFactory.create();
        InventoryController controller = new InventoryController(
                ctx.getInventoryService(),
                ctx.getOrderService()
        );
        String wh = ctx.getWarehouseId();

        printCatalogHelp();

        // ------------------------------------------------------------------
        // FEATURE 1: Restock
        // ------------------------------------------------------------------
        section("FEATURE 1 — Restock (addStock)");
        safe(() -> {
            controller.addStock(wh, "SKU-001", 100);
            controller.addStock(wh, "SKU-002", 50);
            controller.displayStock(wh);
        });

        // ------------------------------------------------------------------
        // FEATURE 2: Create order + reserve
        // ------------------------------------------------------------------
        section("FEATURE 2 — Create order + reserve stock");
        Order[] order1 = new Order[1];
        safe(() -> {
            order1[0] = controller.createOrder(wh, List.of(new OrderLine("SKU-001", 30)));
            System.out.println("Order created: " + order1[0].getId());
            System.out.println("Available SKU-001 after reserve: "
                    + controller.getAvailable(wh, "SKU-001") + " (expected 70)");
            controller.displayStock(wh);
        });

        // ------------------------------------------------------------------
        // FEATURE 3: Fulfill order
        // ------------------------------------------------------------------
        section("FEATURE 3 — Fulfill order (deduct onHand + clear reserved)");
        safe(() -> {
            if (order1[0] != null) {
                controller.fulfillOrder(order1[0].getId());
                System.out.println("Available SKU-001 after fulfill: "
                        + controller.getAvailable(wh, "SKU-001") + " (expected 70)");
                controller.displayStock(wh);
            }
        });

        // ------------------------------------------------------------------
        // FAIL PATH: Oversell blocked at reserve time
        // ------------------------------------------------------------------
        section("FAIL PATH — Oversell blocked (order 80 when available 70)");
        safe(() -> {
            controller.createOrder(wh, List.of(new OrderLine("SKU-001", 80)));
        });
        controller.displayStock(wh);

        // ------------------------------------------------------------------
        // Cancel path: reservation released
        // ------------------------------------------------------------------
        section("CANCEL PATH — Create order then cancel (reserved → available)");
        Order[] order2 = new Order[1];
        safe(() -> {
            order2[0] = controller.createOrder(wh, List.of(new OrderLine("SKU-001", 20)));
            System.out.println("Before cancel — available: "
                    + controller.getAvailable(wh, "SKU-001") + " (expected 50)");
            controller.cancelOrder(order2[0].getId());
            System.out.println("After cancel — available: "
                    + controller.getAvailable(wh, "SKU-001") + " (expected 70)");
        });

        // ------------------------------------------------------------------
        // Low stock alert (Observer)
        // ------------------------------------------------------------------
        section("OBSERVER — Low stock alert (fulfill until onHand <= threshold)");
        safe(() -> {
            Order bigOrder = controller.createOrder(wh, List.of(new OrderLine("SKU-001", 65)));
            controller.fulfillOrder(bigOrder.getId());
            // onHand should be 5, threshold is 10 → LowStockNotifier fires
            controller.displayStock(wh);
        });

        // ------------------------------------------------------------------
        // Invalid product
        // ------------------------------------------------------------------
        section("FAIL PATH — Unknown SKU");
        safe(() -> controller.addStock(wh, "SKU-999", 10));

        // ------------------------------------------------------------------
        // Audit log
        // ------------------------------------------------------------------
        section("Audit trail (Command pattern output)");
        controller.displayMovementLog();

        System.out.println("\nDemo complete. Re-read models/InventoryItem.java — that is the heart of this LLD.");
        System.out.println("For hands-on practice, run: com.inventory.inventorymanagement.demo.InventoryDemo");
    }

    private static void printCatalogHelp() {
        System.out.println("Demo catalog (from CatalogFactory):");
        System.out.println("  SKU-001  Wireless Mouse  reorder threshold 10");
        System.out.println("  SKU-002  USB-C Hub       reorder threshold 5");
        System.out.println("  SKU-003  Laptop Stand     reorder threshold 8");
        System.out.println("  Warehouse: WH-001\n");
    }

    private static void section(String title) {
        System.out.println("\n>>> " + title);
        System.out.println("------------------------------------------------");
    }

    /**
     * Catch domain exceptions so one failed scenario does not kill the whole demo.
     */
    private static void safe(Runnable action) {
        try {
            action.run();
        } catch (RuntimeException ex) {
            System.out.println("!! " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }
}
