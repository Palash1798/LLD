package com.chessgame.chess.model.Pieces;

import com.chessgame.chess.enums.Color;
import com.chessgame.chess.enums.PieceType;
import com.chessgame.chess.model.Board;
import com.chessgame.chess.model.Position;

/**
 * Rook: any number of squares horizontally OR vertically.
 * Path between from and to must be clear.
 */
public class Rook extends Piece {

    public Rook(Color color) {
        super(color, PieceType.ROOK);
    }

    @Override
    public boolean canMove(Board board, Position from, Position to) {
        // Step 1: destination ok?
        if (!isDestinationValid(board, to)) {
            return false;
        }

        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getCol() - from.getCol();

        // Step 2: must stay on same row OR same column (not both zero)
        boolean sameRow = rowDiff == 0 && colDiff != 0;
        boolean sameCol = colDiff == 0 && rowDiff != 0;
        if (!sameRow && !sameCol) {
            return false;
        }

        // Step 3: no piece blocking the path
        return board.isPathClear(from, to);
    }
}
