package com.chessgame.chess.exceptions;

/**
 * Thrown when the chosen move breaks chess rules
 * (wrong geometry, blocked path, empty source, etc.).
 */
public class InvalidMoveException extends RuntimeException {

    public InvalidMoveException(String message) {
        super(message);
    }
}
