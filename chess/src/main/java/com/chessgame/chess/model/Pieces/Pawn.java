package com.chessgame.chess.model.Pieces;

import com.chessgame.chess.enums.Color;
import com.chessgame.chess.enums.PieceType;
import com.chessgame.chess.model.Board;
import com.chessgame.chess.model.Position;

/**
 * Pawn rules (simplified MVP):
 * - Moves forward 1 square if empty
 * - From start rank, can move forward 2 if both squares empty
 * - Captures diagonally forward 1 square
 *
 * White moves UP the board (row increases).
 * Black moves DOWN the board (row decreases).
 */
public class Pawn extends Piece {

    public Pawn(Color color) {
        super(color, PieceType.PAWN);
    }

    @Override
    public boolean canMove(Board board, Position from, Position to) {
        // Step 1: shared destination rules (on board, not own piece)
        if (!isDestinationValid(board, to)) {
            return false;
        }

        int fromRow = from.getRow();
        int fromCol = from.getCol();
        int toRow = to.getRow();
        int toCol = to.getCol();

        // Step 2: White moves +1 row, Black moves -1 row
        int direction = (getColor() == Color.WHITE) ? 1 : -1;
        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;

        Piece destinationPiece = board.getPiece(to);

        // Step 3: forward move (same column) — destination must be EMPTY
        if (colDiff == 0 && destinationPiece == null) {
            // 3a: single step forward
            if (rowDiff == direction) {
                return true;
            }

            // 3b: double step from starting rank
            int startRow = (getColor() == Color.WHITE) ? 1 : 6;
            if (!hasMoved() && fromRow == startRow && rowDiff == 2 * direction) {
                // middle square must also be empty
                Position middle = new Position(fromRow + direction, fromCol);
                return board.getPiece(middle) == null;
            }
        }

        // Step 4: diagonal capture — must land on ENEMY piece
        if (Math.abs(colDiff) == 1 && rowDiff == direction && destinationPiece != null) {
            return destinationPiece.getColor() != getColor();
        }

        // Step 5: anything else is illegal for a pawn
        return false;
    }
}
