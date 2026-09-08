package com.chessgame.chess.model.Pieces;

import com.chessgame.chess.enums.Color;
import com.chessgame.chess.enums.PieceType;
import com.chessgame.chess.model.Board;
import com.chessgame.chess.model.Position;

/**
 * Queen: Rook rules OR Bishop rules (any direction, path clear).
 */
public class Queen extends Piece {

    public Queen(Color color) {
        super(color, PieceType.QUEEN);
    }

    @Override
    public boolean canMove(Board board, Position from, Position to) {
        // Step 1: destination ok?
        if (!isDestinationValid(board, to)) {
            return false;
        }

        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());

        // Step 2: rook-like (same row or same col) OR bishop-like (diagonal)
        boolean rookMove = (rowDiff == 0 && colDiff != 0) || (colDiff == 0 && rowDiff != 0);
        boolean bishopMove = rowDiff != 0 && rowDiff == colDiff;

        if (!rookMove && !bishopMove) {
            return false;
        }

        // Step 3: path clear
        return board.isPathClear(from, to);
    }
}
