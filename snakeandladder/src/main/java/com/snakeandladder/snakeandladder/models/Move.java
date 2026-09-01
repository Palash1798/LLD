package com.snakeandladder.snakeandladder.models;

import lombok.Getter;

/**
 * Stores one completed move for history / debugging.
 */
@Getter
public class Move {

    private final Player player;
    private final int diceValue;
    private final int fromPosition;
    private final int toPosition;

    public Move(Player player, int diceValue, int fromPosition, int toPosition) {
        this.player = player;
        this.diceValue = diceValue;
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
    }
}
