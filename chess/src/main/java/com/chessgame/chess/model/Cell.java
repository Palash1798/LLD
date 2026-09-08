package com.chessgame.chess.model;

import com.chessgame.chess.enums.CellState;
import com.chessgame.chess.model.Pieces.Piece;

/**
 * One square on the 8×8 board.
 * Holds optional piece + knows if it is empty.
 */
public class Cell {

    private final int row;
    private final int col;
    private Piece piece;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.piece = null; // starts empty
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /** True when no piece is sitting on this square. */
    public boolean isEmpty() {
        return piece == null;
    }

    public CellState getCellState() {
        return isEmpty() ? CellState.EMPTY : CellState.OCCUPIED;
    }
}
