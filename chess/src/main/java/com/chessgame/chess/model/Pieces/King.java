package com.chessgame.chess.model.Pieces;

import com.chessgame.chess.enums.Color;
import com.chessgame.chess.enums.PieceType;
import com.chessgame.chess.model.Board;
import com.chessgame.chess.model.Position;

/**
 * King: one square in any direction (including diagonal).
 * Full check rules are out of scope for MVP.
 */
public class King extends Piece {

    public King(Color color) {
        super(color, PieceType.KING);
    }

    @Override
    public boolean canMove(Board board, Position from, Position to) {
        // Step 1: destination ok?
        if (!isDestinationValid(board, to)) {
            return false;
        }

        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());

        // Step 2: max 1 step in each direction, and must actually move
        boolean moved = rowDiff != 0 || colDiff != 0;
        return moved && rowDiff <= 1 && colDiff <= 1;
    }
}
