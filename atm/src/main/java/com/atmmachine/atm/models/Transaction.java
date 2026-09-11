package com.atmmachine.atm.models;

import com.atmmachine.atm.enums.TransactionType;

import java.time.Instant;

/**
 * Optional transaction history (like Move history in Chess / purchase log in Vending).
 */
public class Transaction {

    private final TransactionType type;
    private final int amount;
    private final String accountId;
    private final String status;
    private final Instant timestamp;

    public Transaction(TransactionType type, int amount, String accountId, String status) {
        this.type = type;
        this.amount = amount;
        this.accountId = accountId;
        this.status = status;
        this.timestamp = Instant.now();
    }

    public TransactionType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getStatus() {
        return status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Txn{" + type + ", amount=" + amount
                + ", account=" + accountId
                + ", status=" + status
                + ", at=" + timestamp + "}";
    }
}
