package com.inventory.inventorymanagement.enums;

/**
 * Step 1 — Order lifecycle states (draw this on whiteboard first).
 *
 * PENDING   → stock is reserved but not yet shipped
 * FULFILLED → on-hand reduced, reservation cleared
 * CANCELLED → reservation released, on-hand unchanged
 */
public enum OrderStatus {
    PENDING,
    FULFILLED,
    CANCELLED
}
