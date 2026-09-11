package com.atmmachine.atm.states;

import com.atmmachine.atm.enums.AtmSessionStatus;
import com.atmmachine.atm.models.ATM;

/**
 * STATE PATTERN — State interface.
 *
 * Each concrete state decides what insertCard / enterPin / checkBalance / withdraw / ejectCard mean.
 * ATM (Context) just delegates here — no giant if-else on status.
 *
 * Transitions (happy path):
 *   Idle --insertCard--> HasCard --enterPin--> Authenticated --withdraw--> Dispensing --done--> Authenticated
 *   Authenticated --ejectCard--> Idle
 */
public interface AtmState {

    void insertCard(ATM atm, String cardNumber);

    void enterPin(ATM atm, String pin);

    int checkBalance(ATM atm);

    void withdraw(ATM atm, int amount);

    void ejectCard(ATM atm);

    /** For display / logging — which logical status am I? */
    AtmSessionStatus getStatus();
}
