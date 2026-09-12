package com.inventory.inventorymanagement.observers;

import com.inventory.inventorymanagement.models.InventoryItem;

/**
 * Step 6 — OBSERVER pattern interface.
 *
 * InventoryService notifies observers after onHand changes (restock or fulfill).
 * Keeps alert/email/Slack logic out of the core service (Open/Closed principle).
 */
public interface InventoryObserver {

    void onStockChanged(InventoryItem item);
}
