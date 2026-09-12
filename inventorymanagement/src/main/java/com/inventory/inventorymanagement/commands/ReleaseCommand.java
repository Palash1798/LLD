package com.inventory.inventorymanagement.commands;

import com.inventory.inventorymanagement.enums.MovementType;
import com.inventory.inventorymanagement.models.InventoryItem;
import com.inventory.inventorymanagement.models.StockMovement;

/**
 * Step 5c — Release command: order cancelled — decreases quantityReserved only.
 */
public class ReleaseCommand implements StockMovementCommand {

    private final InventoryItem item;
    private final int quantity;
    private final String orderId;

    public ReleaseCommand(InventoryItem item, int quantity, String orderId) {
        this.item = item;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    @Override
    public StockMovement execute() {
        item.release(quantity);
        return new StockMovement(
                MovementType.RELEASE,
                item.getWarehouseId(),
                item.getProductId(),
                quantity,
                orderId
        );
    }
}
