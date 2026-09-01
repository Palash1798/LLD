package com.snakeandladder.snakeandladder.strategies;

import java.util.Random;

/**
 * Standard dice: returns a random number between 1 and 6.
 */
public class RandomDiceRollStrategy implements DiceRollStrategy {

    private final Random random = new Random();

    @Override
    public int roll() {
        return random.nextInt(6) + 1;
    }
}
