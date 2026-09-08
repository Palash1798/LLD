package com.chessgame.chess.model;

import com.chessgame.chess.enums.Color;
import com.chessgame.chess.enums.GameState;
import com.chessgame.chess.enums.PieceType;
import com.chessgame.chess.exceptions.GameAlreadyEndedException;
import com.chessgame.chess.exceptions.InvalidMoveException;
import com.chessgame.chess.exceptions.WrongTurnException;
import com.chessgame.chess.model.Pieces.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * Core orchestrator for one chess match.
 * Owns board, players, turn, move history, and win detection (MVP: king captured).
 */
public class Game {

    private final Board board;
    private final Player white;
    private final Player black;
    private Player currentPlayer;
    private GameState gameState;
    private Player winner;
    private final List<Move> moves;

    public Game(Player white, Player black) {
        // Step 1: validate colors
        if (white.getColor() != Color.WHITE || black.getColor() != Color.BLACK) {
            throw new IllegalArgumentException("Players must be White and Black respectively");
        }

        // Step 2: create board with standard setup
        this.board = new Board();
        this.white = white;
        this.black = black;

        // Step 3: White always starts
        this.currentPlayer = white;
        this.gameState = GameState.IN_PROGRESS;
        this.winner = null;
        this.moves = new ArrayList<>();
    }

    public Board getBoard() {
        return board;
    }

    public Player getWhite() {
        return white;
    }

    public Player getBlack() {
        return black;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Player getWinner() {
        return winner;
    }

    public List<Move> getMoves() {
        return moves;
    }

    /**
     * Attempt one move from → to (algebraic strings like "e2", "e4").
     */
    public void makeMove(String fromAlgebraic, String toAlgebraic) {
        Position from = Position.fromAlgebraic(fromAlgebraic);
        Position to = Position.fromAlgebraic(toAlgebraic);
        makeMove(from, to);
    }

    /**
     * Core move pipeline — read this method carefully for interviews.
     */
    public void makeMove(Position from, Position to) {
        // Step 1: game must still be running
        if (gameState != GameState.IN_PROGRESS) {
            throw new GameAlreadyEndedException("Game is already over. State = " + gameState);
        }

        // Step 2: there must be a piece on the source square
        Piece piece = board.getPiece(from);
        if (piece == null) {
            throw new InvalidMoveException("No piece at " + from);
        }

        // Step 3: that piece must belong to the current player
        if (piece.getColor() != currentPlayer.getColor()) {
            throw new WrongTurnException(
                    "It is " + currentPlayer.getColor() + "'s turn. Cannot move " + piece.getColor()
                            + " piece at " + from);
        }

        // Step 4: piece-specific geometry / path / capture rules
        if (!piece.canMove(board, from, to)) {
            throw new InvalidMoveException(
                    "Illegal move for " + piece.getType() + ": " + from + " → " + to);
        }

        // Step 5: apply move on the board (may capture)
        Piece captured = board.movePiece(from, to);
        piece.setHasMoved(true);

        // Step 6: record history
        Move move = new Move(currentPlayer, from, to, piece, captured);
        moves.add(move);
        System.out.println(move);

        // Step 7: MVP win condition — capturing the King ends the game
        if (captured != null && captured.getType() == PieceType.KING) {
            winner = currentPlayer;
            gameState = GameState.COMPLETED;
            System.out.println("★ " + winner.getPlayerName() + " wins by capturing the King!");
            return;
        }

        // Step 8: switch turn WHITE ↔ BLACK
        switchTurn();
    }

    private void switchTurn() {
        currentPlayer = (currentPlayer == white) ? black : white;
    }

    /** Optional helper if a player gives up. */
    public void resign(Player player) {
        if (gameState != GameState.IN_PROGRESS) {
            throw new GameAlreadyEndedException("Game is already over");
        }
        winner = (player == white) ? black : white;
        gameState = GameState.COMPLETED;
        System.out.println(player.getPlayerName() + " resigned. Winner: " + winner.getPlayerName());
    }
}
