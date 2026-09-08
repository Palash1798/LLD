package com.chessgame.chess.model.Pieces;

import com.chessgame.chess.enums.Color;
import com.chessgame.chess.enums.PieceType;
import com.chessgame.chess.model.Board;
import com.chessgame.chess.model.Position;

/**
 * Base class for all chess pieces.
 * Subclasses only implement HOW that piece moves (canMove).
 */
public abstract class Piece {

    private final Color color;
    private final PieceType type;
    private boolean hasMoved; // useful for pawn double-step / castling later

    protected Piece(Color color, PieceType type) {
        this.color = color;
        this.type = type;
        this.hasMoved = false;
    }

    public Color getColor() {
        return color;
    }

    public PieceType getType() {
        return type;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    /**
     * Each piece type answers: "Is moving from → to legal on this board?"
     * (Does NOT check whose turn it is — Game does that.)
     */
    public abstract boolean canMove(Board board, Position from, Position to);

    /**
     * Shared checks used by every piece:
     * 1. destination must be on board
     * 2. cannot capture your own piece
     */
    protected boolean isDestinationValid(Board board, Position to) {
        // Step 1: stay inside 0..7
        if (!board.isInside(to)) {
            return false;
        }

        // Step 2: if destination has a piece, it must be opponent
        Piece destinationPiece = board.getPiece(to);
        if (destinationPiece != null && destinationPiece.getColor() == this.color) {
            return false;
        }

        return true;
    }

    /** Short symbol for board printing, e.g. WP, BK */
    public String getSymbol() {
        char colorCode = (color == Color.WHITE) ? 'W' : 'B';
        char typeCode = switch (type) {
            case KING -> 'K';
            case QUEEN -> 'Q';
            case ROOK -> 'R';
            case BISHOP -> 'B';
            case KNIGHT -> 'N';
            case PAWN -> 'P';
        };
        return "" + colorCode + typeCode;
    }
}
