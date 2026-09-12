package com.inventory.inventorymanagement.models;

import com.inventory.inventorymanagement.enums.OrderStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Step 2 — Customer order with one or more line items.
 *
 * Status flow: PENDING → FULFILLED | CANCELLED
 */
public class Order {

    private final String id;
    private final String warehouseId;
    private final List<OrderLine> lines;
    private OrderStatus status;

    public Order(String id, String warehouseId, List<OrderLine> lines) {
        this.id = id;
        this.warehouseId = warehouseId;
        this.lines = new ArrayList<>(lines);
        this.status = OrderStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public List<OrderLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" + id + ", status=" + status + ", warehouse=" + warehouseId + ", lines=" + lines + "}";
    }
}
