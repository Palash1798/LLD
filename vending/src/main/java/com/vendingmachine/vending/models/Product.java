package com.vendingmachine.vending.models;

/**
 * Immutable catalog item sold by the machine.
 *
 * Interview tip: keep money as int (cents/paise) — never float/double.
 */
public class Product {

    private final String code;   // e.g. "A1"
    private final String name;   // e.g. "Coke"
    private final int price;     // e.g. 25 means 25 cents / ₹25 (pick one unit and stay consistent)

    public Product(String code, String name, int price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return code + " | " + name + " | price=" + price;
    }
}
