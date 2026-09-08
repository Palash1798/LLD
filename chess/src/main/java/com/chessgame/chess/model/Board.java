package com.chessgame.chess.model;

import com.chessgame.chess.enums.Color;
import com.chessgame.chess.enums.PieceType;
import com.chessgame.chess.factories.PieceFactory;
import com.chessgame.chess.model.Pieces.Piece;

/**
 * 8×8 chess board.
 * Owns cells, initial setup, path checks, and piece movement on the grid.
 */
public class Board {

    public static final int SIZE = 8;

    private final Cell[][] cells;

    public Board() {
        // Step 1: create empty 8×8 grid
        this.cells = new Cell[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }

        // Step 2: place standard starting pieces
        initialize();
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public Cell getCell(Position position) {
        return cells[position.getRow()][position.getCol()];
    }

    public Piece getPiece(Position position) {
        return getCell(position).getPiece();
    }

    public boolean isInside(Position position) {
        int row = position.getRow();
        int col = position.getCol();
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    /**
     * Place a piece on a square (used during setup).
     */
    public void placePiece(Position position, Piece piece) {
        getCell(position).setPiece(piece);
    }

    /**
     * Move piece from → to.
     * Returns the captured piece (or null if destination was empty).
     */
    public Piece movePiece(Position from, Position to) {
        // Step 1: remember what (if anything) was on destination
        Piece moving = getPiece(from);
        Piece captured = getPiece(to);

        // Step 2: put mover on destination, clear source
        getCell(to).setPiece(moving);
        getCell(from).setPiece(null);

        return captured;
    }

    /**
     * Standard chess starting position.
     *
     * White at rows 0–1, Black at rows 6–7.
     * Back-rank order: R N B Q K B N R
     */
    public void initialize() {
        // Step 1: clear board (safe if called again)
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].setPiece(null);
            }
        }

        // Step 2: place White pieces
        placeBackRank(0, Color.WHITE);
        placePawnRank(1, Color.WHITE);

        // Step 3: place Black pieces
        placeBackRank(7, Color.BLACK);
        placePawnRank(6, Color.BLACK);
    }

    private void placeBackRank(int row, Color color) {
        PieceType[] order = {
                PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN,
                PieceType.KING, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK
        };

        for (int col = 0; col < SIZE; col++) {
            placePiece(new Position(row, col), PieceFactory.create(order[col], color));
        }
    }

    private void placePawnRank(int row, Color color) {
        for (int col = 0; col < SIZE; col++) {
            placePiece(new Position(row, col), PieceFactory.create(PieceType.PAWN, color));
        }
    }

    /**
     * Check that every square STRICTLY BETWEEN from and to is empty.
     * Used by Rook / Bishop / Queen (sliding pieces).
     * Assumes from and to are on the same row, column, or diagonal.
     */
    public boolean isPathClear(Position from, Position to) {
        int rowStep = Integer.compare(to.getRow(), from.getRow()); // -1, 0, or +1
        int colStep = Integer.compare(to.getCol(), from.getCol()); // -1, 0, or +1

        int currentRow = from.getRow() + rowStep;
        int currentCol = from.getCol() + colStep;

        // Walk until we reach the destination square (do not check destination itself)
        while (currentRow != to.getRow() || currentCol != to.getCol()) {
            if (cells[currentRow][currentCol].getPiece() != null) {
                return false; // blocked
            }
            currentRow += rowStep;
            currentCol += colStep;
        }

        return true;
    }

    /**
     * Print board with ranks 8→1 (so it looks like a normal chess diagram).
     * Symbols: WP = white pawn, BK = black king, etc.  "." = empty.
     */
    public void display() {
        System.out.println();
        System.out.println("    a  b  c  d  e  f  g  h");
        System.out.println("  +------------------------+");

        // Print from rank 8 down to rank 1 (row 7 → row 0)
        for (int row = SIZE - 1; row >= 0; row--) {
            System.out.print((row + 1) + " |");
            for (int col = 0; col < SIZE; col++) {
                Piece piece = cells[row][col].getPiece();
                if (piece == null) {
                    System.out.print(" . ");
                } else {
                    System.out.print(" " + piece.getSymbol());
                }
            }
            System.out.println("| " + (row + 1));
        }

        System.out.println("  +------------------------+");
        System.out.println("    a  b  c  d  e  f  g  h");
        System.out.println();
    }
}
