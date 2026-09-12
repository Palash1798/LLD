package com.inventory.inventorymanagement.commands;

import com.inventory.inventorymanagement.enums.MovementType;
import com.inventory.inventorymanagement.models.InventoryItem;
import com.inventory.inventorymanagement.models.StockMovement;

/**
 * Step 5d — Fulfill command: order shipped — decreases BOTH onHand and reserved.
 */
public class FulfillCommand implements StockMovementCommand {

    private final InventoryItem item;
    private final int quantity;
    private final String orderId;

    public FulfillCommand(InventoryItem item, int quantity, String orderId) {
        this.item = item;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    @Override
    public StockMovement execute() {
        item.fulfill(quantity);
        return new StockMovement(
                MovementType.FULFILL,
                item.getWarehouseId(),
                item.getProductId(),
                quantity,
                orderId
        );
    }
}
