package com.inventory.inventorymanagement.observers;

import com.inventory.inventorymanagement.models.InventoryItem;
import com.inventory.inventorymanagement.models.Product;

import java.util.Map;

/**
 * Step 6a — Concrete observer: prints alert when onHand <= reorder threshold.
 *
 * In production this would send email/Slack to procurement team.
 */
public class LowStockNotifier implements InventoryObserver {

    private final Map<String, Product> products;

    public LowStockNotifier(Map<String, Product> products) {
        this.products = products;
    }

    @Override
    public void onStockChanged(InventoryItem item) {
        Product product = products.get(item.getProductId());
        if (product == null) {
            return;
        }
        if (item.getQuantityOnHand() <= product.getReorderThreshold()) {
            System.out.println("*** LOW STOCK ALERT *** "
                    + product.getSku() + " (" + product.getName() + ")"
                    + " at warehouse " + item.getWarehouseId()
                    + " — onHand=" + item.getQuantityOnHand()
                    + ", threshold=" + product.getReorderThreshold()
                    + " → please reorder!");
        }
    }
}
