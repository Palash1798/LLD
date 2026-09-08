package com.chessgame.chess.exceptions;

/**
 * Thrown when a player tries to move a piece that is not theirs,
 * or when it is not their turn.
 */
public class WrongTurnException extends RuntimeException {

    public WrongTurnException(String message) {
        super(message);
    }
}
