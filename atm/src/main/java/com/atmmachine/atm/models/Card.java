package com.atmmachine.atm.models;

/**
 * Debit card that maps to exactly one bank account.
 * PIN is plain text in this study demo — production would store a hash.
 */
public class Card {

    private final String cardNumber;
    private final String accountId;
    private final String pin;

    public Card(String cardNumber, String accountId, String pin) {
        this.cardNumber = cardNumber;
        this.accountId = accountId;
        this.pin = pin;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getPin() {
        return pin;
    }

    @Override
    public String toString() {
        return "Card{" + mask(cardNumber) + " → account " + accountId + "}";
    }

    /** Mask all but last 4 digits for safe logging. */
    private static String mask(String cardNumber) {
        if (cardNumber.length() <= 4) {
            return cardNumber;
        }
        return "****" + cardNumber.substring(cardNumber.length() - 4);
    }
}
