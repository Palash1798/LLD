package com.inventory.inventorymanagement.commands;

import com.inventory.inventorymanagement.enums.MovementType;
import com.inventory.inventorymanagement.models.InventoryItem;
import com.inventory.inventorymanagement.models.StockMovement;

/**
 * Step 5a — Restock command: increases quantityOnHand.
 */
public class StockInCommand implements StockMovementCommand {

    private final InventoryItem item;
    private final int quantity;
    private final String referenceId;

    public StockInCommand(InventoryItem item, int quantity, String referenceId) {
        this.item = item;
        this.quantity = quantity;
        this.referenceId = referenceId;
    }

    @Override
    public StockMovement execute() {
        item.addOnHand(quantity);
        return new StockMovement(
                MovementType.STOCK_IN,
                item.getWarehouseId(),
                item.getProductId(),
                quantity,
                referenceId
        );
    }
}
