package com.atmmachine.atm.exceptions;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(int balance, int amount) {
        super("Insufficient account balance. Balance=" + balance + ", requested=" + amount);
    }
}
