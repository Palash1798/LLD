package com.vendingmachine.vending.models;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Holds all slots keyed by product code.
 *
 * Interview one-liner: "Inventory is Map<code, ItemSlot>."
 */
public class Inventory {

    // LinkedHashMap keeps insertion order so display looks stable in demos
    private final Map<String, ItemSlot> slots = new LinkedHashMap<>();

    /**
     * Step 1: stock a new product (or replace slot for same code).
     */
    public void addProduct(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        slots.put(product.getCode(), new ItemSlot(product, quantity));
    }

    /**
     * Step 2: lookup by code (null if unknown — caller decides exception).
     */
    public ItemSlot getSlot(String code) {
        return slots.get(code);
    }

    public boolean isAvailable(String code) {
        ItemSlot slot = slots.get(code);
        return slot != null && slot.isAvailable();
    }

    /**
     * Step 3: after payment committed, reduce quantity by 1.
     */
    public void decrement(String code) {
        ItemSlot slot = slots.get(code);
        if (slot == null) {
            throw new IllegalArgumentException("Unknown product code: " + code);
        }
        slot.decrement();
    }

    /**
     * Used to decide SoldOutState after a dispense.
     */
    public boolean isAllSoldOut() {
        if (slots.isEmpty()) {
            return true;
        }
        for (ItemSlot slot : slots.values()) {
            if (slot.isAvailable()) {
                return false;
            }
        }
        return true;
    }

    public Collection<ItemSlot> getAllSlots() {
        return Collections.unmodifiableCollection(slots.values());
    }

    /**
     * Pretty print for CLI / interview demo.
     */
    public void display() {
        System.out.println("---------- INVENTORY ----------");
        System.out.printf("%-6s %-12s %-8s %-6s%n", "CODE", "NAME", "PRICE", "QTY");
        for (ItemSlot slot : slots.values()) {
            Product p = slot.getProduct();
            System.out.printf("%-6s %-12s %-8d %-6d%n",
                    p.getCode(), p.getName(), p.getPrice(), slot.getQuantity());
        }
        System.out.println("-------------------------------");
    }
}
