package com.atmmachine.atm.exceptions;

public class InsufficientCashException extends RuntimeException {

    public InsufficientCashException(int availableCash, int amount) {
        super("ATM does not have enough cash. Available=" + availableCash + ", requested=" + amount);
    }
}
