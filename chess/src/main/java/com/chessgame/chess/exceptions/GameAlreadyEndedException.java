package com.chessgame.chess.exceptions;

/**
 * Thrown when someone tries to move after the game is already over.
 */
public class GameAlreadyEndedException extends RuntimeException {

    public GameAlreadyEndedException(String message) {
        super(message);
    }
}
