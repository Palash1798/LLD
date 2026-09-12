package com.inventory.inventorymanagement.exceptions;

/**
 * Thrown when a SKU does not exist in the product catalog.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String sku) {
        super("Product not found: " + sku);
    }
}
