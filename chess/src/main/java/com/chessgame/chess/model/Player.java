package com.chessgame.chess.model;

import com.chessgame.chess.enums.Color;

/**
 * A human player in the match — name + which color they play.
 */
public class Player {

    private final String playerName;
    private final Color color;

    public Player(String playerName, Color color) {
        this.playerName = playerName;
        this.color = color;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return playerName + " (" + color + ")";
    }
}
