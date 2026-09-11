package com.vendingmachine.vending.models;

import java.time.Instant;

/**
 * Optional purchase history record (like Move history in Chess / Snake & Ladder).
 * Nice for schema talk; not required for core State demo.
 */
public class Transaction {

    private final String productCode;
    private final String productName;
    private final int amountPaid;
    private final int change;
    private final Instant timestamp;

    public Transaction(String productCode, String productName, int amountPaid, int change) {
        this.productCode = productCode;
        this.productName = productName;
        this.amountPaid = amountPaid;
        this.change = change;
        this.timestamp = Instant.now();
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public int getChange() {
        return change;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Txn{" + productCode + " " + productName
                + ", paid=" + amountPaid
                + ", change=" + change
                + ", at=" + timestamp + "}";
    }
}
