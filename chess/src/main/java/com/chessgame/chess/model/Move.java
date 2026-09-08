package com.chessgame.chess.model;

import com.chessgame.chess.model.Pieces.Piece;

/**
 * One completed (or attempted) move in history.
 * Useful for printing, undo later, and debugging.
 */
public class Move {

    private final Player player;
    private final Position from;
    private final Position to;
    private final Piece piece;
    private final Piece captured; // null if destination was empty

    public Move(Player player, Position from, Position to, Piece piece, Piece captured) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.piece = piece;
        this.captured = captured;
    }

    public Player getPlayer() {
        return player;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Piece getPiece() {
        return piece;
    }

    public Piece getCaptured() {
        return captured;
    }

    @Override
    public String toString() {
        String captureText = (captured == null) ? "" : " x " + captured.getType();
        return player.getPlayerName() + ": " + piece.getType()
                + " " + from + " → " + to + captureText;
    }
}
