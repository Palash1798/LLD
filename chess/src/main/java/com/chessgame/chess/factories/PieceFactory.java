package com.chessgame.chess.factories;

import com.chessgame.chess.enums.Color;
import com.chessgame.chess.enums.PieceType;
import com.chessgame.chess.model.Pieces.Bishop;
import com.chessgame.chess.model.Pieces.King;
import com.chessgame.chess.model.Pieces.Knight;
import com.chessgame.chess.model.Pieces.Pawn;
import com.chessgame.chess.model.Pieces.Piece;
import com.chessgame.chess.model.Pieces.Queen;
import com.chessgame.chess.model.Pieces.Rook;

/**
 * Creates Piece objects from a type + color.
 * Keeps Board.initialize() free of big if-else / switch soup.
 */
public class PieceFactory {

    private PieceFactory() {
        // utility class — no instances
    }

    /**
     * Step: map PieceType → concrete subclass.
     */
    public static Piece create(PieceType type, Color color) {
        return switch (type) {
            case KING -> new King(color);
            case QUEEN -> new Queen(color);
            case ROOK -> new Rook(color);
            case BISHOP -> new Bishop(color);
            case KNIGHT -> new Knight(color);
            case PAWN -> new Pawn(color);
        };
    }
}
