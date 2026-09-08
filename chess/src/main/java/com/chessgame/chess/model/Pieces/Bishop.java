package com.chessgame.chess.model.Pieces;

import com.chessgame.chess.enums.Color;
import com.chessgame.chess.enums.PieceType;
import com.chessgame.chess.model.Board;
import com.chessgame.chess.model.Position;

/**
 * Bishop: any number of squares diagonally.
 * |Δrow| must equal |Δcol|, and path must be clear.
 */
public class Bishop extends Piece {

    public Bishop(Color color) {
        super(color, PieceType.BISHOP);
    }

    @Override
    public boolean canMove(Board board, Position from, Position to) {
        // Step 1: destination ok?
        if (!isDestinationValid(board, to)) {
            return false;
        }

        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());

        // Step 2: must be a true diagonal (and actually move)
        if (rowDiff == 0 || rowDiff != colDiff) {
            return false;
        }

        // Step 3: path clear
        return board.isPathClear(from, to);
    }
}
