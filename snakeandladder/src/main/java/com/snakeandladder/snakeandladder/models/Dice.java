package com.snakeandladder.snakeandladder.models;

import com.snakeandladder.snakeandladder.strategies.DiceRollStrategy;
import lombok.Getter;

/**
 * Dice delegates the actual roll logic to DiceRollStrategy.
 */
@Getter
public class Dice {

    private final DiceRollStrategy diceRollStrategy;

    public Dice(DiceRollStrategy diceRollStrategy) {
        this.diceRollStrategy = diceRollStrategy;
    }

    public int roll() {
        return diceRollStrategy.roll();
    }
}
