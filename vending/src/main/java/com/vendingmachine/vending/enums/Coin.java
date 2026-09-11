package com.vendingmachine.vending.enums;

/**
 * Optional denomination helper.
 * MVP can just use insertMoney(int). Coin is useful if interviewer asks "what coins do you accept?"
 */
public enum Coin {

    NICKEL(5),
    DIME(10),
    QUARTER(25),
    DOLLAR(100);

    private final int value;

    Coin(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
