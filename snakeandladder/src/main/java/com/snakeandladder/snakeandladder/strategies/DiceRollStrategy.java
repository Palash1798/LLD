package com.snakeandladder.snakeandladder.strategies;

/**
 * Strategy pattern for dice rolling.
 * We can swap random dice with fixed dice while testing.
 */
public interface DiceRollStrategy {

    int roll();
}
