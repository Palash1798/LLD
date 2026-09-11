package com.vendingmachine.vending.factories;

import com.vendingmachine.vending.models.Inventory;
import com.vendingmachine.vending.models.Product;

/**
 * Seeds a demo inventory so CLI / interview walkthrough starts with real products.
 *
 * Prices are integers (e.g. cents or rupees — pick one unit and stay consistent).
 */
public final class ProductFactory {

    private ProductFactory() {
        // utility
    }

    /**
     * Default stock for study demos.
     */
    public static Inventory createDefaultInventory() {
        Inventory inventory = new Inventory();

        // Step: add a few products with different prices / quantities
        inventory.addProduct(new Product("A1", "Coke", 25), 5);
        inventory.addProduct(new Product("A2", "Pepsi", 35), 3);
        inventory.addProduct(new Product("B1", "Water", 20), 4);
        inventory.addProduct(new Product("B2", "Chips", 45), 2);

        return inventory;
    }

    /**
     * Tiny inventory useful for SoldOut demos (qty=1 each).
     */
    public static Inventory createTinyInventory() {
        Inventory inventory = new Inventory();
        inventory.addProduct(new Product("A1", "Coke", 25), 1);
        inventory.addProduct(new Product("B1", "Water", 20), 1);
        return inventory;
    }
}
