package com.inventory.inventorymanagement.commands;

import com.inventory.inventorymanagement.enums.MovementType;
import com.inventory.inventorymanagement.models.InventoryItem;
import com.inventory.inventorymanagement.models.StockMovement;

/**
 * Step 5b — Reserve command: increases quantityReserved (oversell guard inside item.reserve).
 */
public class ReserveCommand implements StockMovementCommand {

    private final InventoryItem item;
    private final int quantity;
    private final String orderId;

    public ReserveCommand(InventoryItem item, int quantity, String orderId) {
        this.item = item;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    @Override
    public StockMovement execute() {
        item.reserve(quantity);
        return new StockMovement(
                MovementType.RESERVE,
                item.getWarehouseId(),
                item.getProductId(),
                quantity,
                orderId
        );
    }
}
