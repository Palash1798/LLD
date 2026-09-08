package com.chessgame.chess.model;

/**
 * A square on the board identified by row and column.
 *
 * Coordinate convention used in this project:
 * - row 0 = White's back rank (algebraic rank 1)
 * - row 7 = Black's back rank (algebraic rank 8)
 * - col 0 = file 'a'
 * - col 7 = file 'h'
 *
 * Example: "e2" → row=1, col=4
 *
 * NOTE: Position is ONLY coordinates. The piece lives on Cell, not here.
 */
public class Position {

    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * Convert algebraic chess notation (like "e2") into a Position.
     * Step 1: read file letter → column
     * Step 2: read rank digit → row
     */
    public static Position fromAlgebraic(String algebraic) {
        // Step 1: basic format check ("e2" must be length 2)
        if (algebraic == null || algebraic.length() != 2) {
            throw new IllegalArgumentException("Position must look like e2, got: " + algebraic);
        }

        String normalized = algebraic.trim().toLowerCase();
        char file = normalized.charAt(0); // a-h
        char rank = normalized.charAt(1); // 1-8

        // Step 2: convert file 'a'..'h' → col 0..7
        int col = file - 'a';

        // Step 3: convert rank '1'..'8' → row 0..7
        int row = rank - '1';

        // Step 4: validate bounds
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            throw new IllegalArgumentException("Position out of board: " + algebraic);
        }

        return new Position(row, col);
    }

    /** Convert back to algebraic, e.g. (1,4) → "e2" */
    public String toAlgebraic() {
        char file = (char) ('a' + col);
        char rank = (char) ('1' + row);
        return "" + file + rank;
    }

    @Override
    public String toString() {
        return toAlgebraic();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Position other)) {
            return false;
        }
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }
}
