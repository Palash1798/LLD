package com.snakeandladder.snakeandladder.models;

import lombok.Getter;
import lombok.Setter;

/**
 * A player token on the board.
 * Position 0 means the player has not entered the board yet.
 */
@Getter
@Setter
public class Player {

    private int id;
    private String name;
    private int currentPosition;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.currentPosition = 0;
    }
}
