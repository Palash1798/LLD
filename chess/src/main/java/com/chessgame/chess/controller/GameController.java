package com.chessgame.chess.controller;

import com.chessgame.chess.enums.Color;
import com.chessgame.chess.enums.GameState;
import com.chessgame.chess.model.Game;
import com.chessgame.chess.model.Player;

/**
 * Thin, almost-stateless entry point.
 * Main / API talks to Controller; Controller forwards to Game.
 */
public class GameController {

    /**
     * Create a new game with two named players.
     */
    public Game startGame(String whiteName, String blackName) {
        // Step 1: build players with fixed colors
        Player white = new Player(whiteName, Color.WHITE);
        Player black = new Player(blackName, Color.BLACK);

        // Step 2: Game constructor builds board + sets White to move
        return new Game(white, black);
    }

    /**
     * Perform one move using algebraic squares, e.g. "e2", "e4".
     */
    public void makeMove(Game game, String from, String to) {
        game.makeMove(from, to);
    }

    public void displayBoard(Game game) {
        game.getBoard().display();
    }

    public GameState getGameState(Game game) {
        return game.getGameState();
    }

    public Player getWinner(Game game) {
        return game.getWinner();
    }

    public Player getCurrentPlayer(Game game) {
        return game.getCurrentPlayer();
    }

    public void resign(Game game, Player player) {
        game.resign(player);
    }
}
