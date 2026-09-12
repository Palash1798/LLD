package com.inventory.inventorymanagement.demo;

import com.inventory.inventorymanagement.controller.InventoryController;
import com.inventory.inventorymanagement.factories.CatalogContext;
import com.inventory.inventorymanagement.factories.CatalogFactory;
import com.inventory.inventorymanagement.models.OrderLine;

import java.util.List;
import java.util.Scanner;

/**
 * Interactive CLI for hands-on practice.
 *
 * Use this when you want to drive inventory ops yourself step-by-step
 * instead of watching the scripted InventorymanagementApplication demo.
 *
 * Study flow: restock → check stock → create order → fulfill/cancel
 */
public class InventoryDemo {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  INVENTORY MANAGEMENT — INTERACTIVE DEMO");
        System.out.println("  Patterns: Command + Observer");
        System.out.println("========================================");
        printCatalogHelp();

        CatalogContext ctx = CatalogFactory.create();
        InventoryController controller = new InventoryController(
                ctx.getInventoryService(),
                ctx.getOrderService()
        );
        String warehouseId = ctx.getWarehouseId();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Product SKU: ");
                        String sku = scanner.nextLine().trim();
                        System.out.print("Quantity to add: ");
                        int qty = Integer.parseInt(scanner.nextLine().trim());
                        controller.addStock(warehouseId, sku, qty);
                    }
                    case "2" -> {
                        System.out.print("Product SKU: ");
                        String sku = scanner.nextLine().trim();
                        int available = controller.getAvailable(warehouseId, sku);
                        System.out.println("Available: " + available);
                    }
                    case "3" -> controller.displayStock(warehouseId);
                    case "4" -> {
                        System.out.print("Product SKU: ");
                        String sku = scanner.nextLine().trim();
                        System.out.print("Order quantity: ");
                        int qty = Integer.parseInt(scanner.nextLine().trim());
                        var order = controller.createOrder(warehouseId, List.of(new OrderLine(sku, qty)));
                        System.out.println("Created: " + order.getId());
                    }
                    case "5" -> {
                        System.out.print("Order ID: ");
                        controller.fulfillOrder(scanner.nextLine().trim());
                    }
                    case "6" -> {
                        System.out.print("Order ID: ");
                        controller.cancelOrder(scanner.nextLine().trim());
                    }
                    case "7" -> controller.displayMovementLog();
                    case "8" -> running = false;
                    default -> System.out.println("Invalid choice. Pick 1-8.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("!! Invalid number format.");
            } catch (RuntimeException ex) {
                System.out.println("!! " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            }
            System.out.println();
        }

        scanner.close();
        System.out.println("Goodbye.");
    }

    private static void printCatalogHelp() {
        System.out.println("Demo SKUs:");
        System.out.println("  SKU-001  Wireless Mouse  (reorder at 10)");
        System.out.println("  SKU-002  USB-C Hub       (reorder at 5)");
        System.out.println("  SKU-003  Laptop Stand    (reorder at 8)");
        System.out.println("Warehouse: WH-001\n");
    }

    private static void printMenu() {
        System.out.println("--- Menu ---");
        System.out.println("1. Restock (add stock)");
        System.out.println("2. Check available qty for SKU");
        System.out.println("3. Display all stock");
        System.out.println("4. Create order (reserve stock)");
        System.out.println("5. Fulfill order");
        System.out.println("6. Cancel order");
        System.out.println("7. Show movement audit log");
        System.out.println("8. Exit");
    }
}
