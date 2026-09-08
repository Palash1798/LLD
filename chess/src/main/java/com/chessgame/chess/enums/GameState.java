package com.chessgame.chess.enums;

/**
 * Lifecycle of one chess match.
 * MVP ends with COMPLETED when a King is captured.
 */
public enum GameState {
    IN_PROGRESS,
    COMPLETED,   // someone won (king captured / resign)
    DRAW
}
