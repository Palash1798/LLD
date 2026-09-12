package com.splitwise.splitwise.models;

/**
 * Step 2a — Gross debt between THIS user and one counterparty.
 *
 * Both directions can be non-zero before netting:
 *   net from this user's view = youGetBackCents - youOweCents
 *
 * Stored inside {@link BalanceSheet#balancesByUser} keyed by counterparty userId.
 */
public class PairwiseBalance {

    /** How much THIS user owes the counterparty. */
    private int youOweCents;

    /** How much the counterparty owes THIS user. */
    private int youGetBackCents;

    public PairwiseBalance() {
    }

    public int getYouOweCents() {
        return youOweCents;
    }

    public void addYouOwe(int cents) {
        this.youOweCents += cents;
    }

    public int getYouGetBackCents() {
        return youGetBackCents;
    }

    public void addYouGetBack(int cents) {
        this.youGetBackCents += cents;
    }

    /** Net from this user's perspective: positive = counterparty owes you. */
    public int netCents() {
        return youGetBackCents - youOweCents;
    }

    @Override
    public String toString() {
        return "PairwiseBalance{owe=" + youOweCents + ", getBack=" + youGetBackCents + ", net=" + netCents() + "}";
    }
}
