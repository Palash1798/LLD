package com.inventory.inventorymanagement.enums;

/**
 * Step 1 — Types of stock movements for the audit log (Command pattern).
 *
 * STOCK_IN → warehouse receives shipment (onHand increases)
 * RESERVE  → order holds stock (reserved increases)
 * RELEASE  → order cancelled (reserved decreases)
 * FULFILL  → order shipped (onHand and reserved both decrease)
 */
public enum MovementType {
    STOCK_IN,
    RESERVE,
    RELEASE,
    FULFILL
}
