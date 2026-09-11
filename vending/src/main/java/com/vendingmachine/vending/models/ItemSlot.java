package com.vendingmachine.vending.models;

/**
 * One physical slot in the machine: a product + how many units are left.
 *
 * Why separate from Product?
 * - Product = catalog (code, name, price)
 * - ItemSlot = machine stock for that product
 */
public class ItemSlot {

    private final Product product;
    private int quantity;

    public ItemSlot(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailable() {
        return quantity > 0;
    }

    /**
     * Step: reduce stock by 1 after a successful purchase.
     */
    public void decrement() {
        if (quantity <= 0) {
            throw new IllegalStateException("Cannot decrement empty slot: " + product.getCode());
        }
        quantity--;
    }

    /**
     * Optional admin/restock helper (extension).
     */
    public void addQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Restock amount must be positive");
        }
        quantity += amount;
    }

    @Override
    public String toString() {
        return product + " | qty=" + quantity;
    }
}
