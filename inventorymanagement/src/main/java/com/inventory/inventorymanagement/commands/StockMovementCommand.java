package com.inventory.inventorymanagement.commands;

import com.inventory.inventorymanagement.models.StockMovement;

/**
 * Step 5 — COMMAND pattern interface.
 *
 * Each stock operation (in, reserve, release, fulfill) is a command that:
 *   1) validates input
 *   2) mutates InventoryItem
 *   3) returns a StockMovement for the audit log
 *
 * Interview tip: mention undo/compensation — ReleaseCommand undoes ReserveCommand.
 */
public interface StockMovementCommand {

    StockMovement execute();
}
