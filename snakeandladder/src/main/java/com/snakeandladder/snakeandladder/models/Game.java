package com.snakeandladder.snakeandladder.models;

import com.snakeandladder.snakeandladder.boardConfig.DefaultBoardConfigStrategy;
import com.snakeandladder.snakeandladder.enums.GameState;
import com.snakeandladder.snakeandladder.enums.JumperType;
import com.snakeandladder.snakeandladder.exceptions.InvalidPlayerCountException;
import com.snakeandladder.snakeandladder.strategies.RandomDiceRollStrategy;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Core game class.
 * Owns board, players, dice, turn order, and winner.
 */
@Getter
public class Game {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;
    private static final int DEFAULT_BOARD_SIZE = 100;

    private final Board board;
    private final List<Player> players;
    private final Dice dice;
    private final List<Move> moves;

    private GameState gameState;
    private Player winner;
    private int currentPlayerIndex;

    public Game(List<Player> players, Board board, Dice dice) {
        validatePlayers(players);

        this.board = board;
        this.players = players;
        this.dice = dice;
        this.moves = new ArrayList<>();

        // Initial game state
        this.gameState = GameState.IN_PROGRESS;
        this.winner = null;
        this.currentPlayerIndex = 0;
    }

    /**
     * Helper factory for quick setup with default board and random dice.
     */
    public static Game createDefault(List<Player> players) {
        DefaultBoardConfigStrategy boardConfig = new DefaultBoardConfigStrategy();
        Board board = new Board(DEFAULT_BOARD_SIZE, boardConfig.getJumpers());
        Dice dice = new Dice(new RandomDiceRollStrategy());
        return new Game(players, board, dice);
    }

    /**
     * One full turn for the current player.
     */
    public void playTurn() {
        // Step 1: Stop if game is already over
        if (gameState != GameState.IN_PROGRESS) {
            return;
        }

        // Step 2: Get current player and roll dice
        Player currentPlayer = players.get(currentPlayerIndex);
        int diceValue = dice.roll();
        int fromPosition = currentPlayer.getCurrentPosition();

        System.out.println("\n" + currentPlayer.getName() + " rolled " + diceValue
                + " (current position: " + formatPosition(fromPosition) + ")");

        // Step 3: Calculate next position using board rules
        int nextPosition = calculateNextPosition(fromPosition, diceValue);

        // Step 4: If no move happened, pass turn to next player
        if (nextPosition == fromPosition) {
            System.out.println("No move. Need exact count to finish.");
            moveToNextPlayer();
            return;
        }

        // Step 5: Apply snake or ladder if present on landed cell
        int finalPosition = board.getDestination(nextPosition);
        printJumperMessage(nextPosition, finalPosition);

        // Step 6: Update player position and save move history
        currentPlayer.setCurrentPosition(finalPosition);
        moves.add(new Move(currentPlayer, diceValue, fromPosition, finalPosition));

        System.out.println(currentPlayer.getName() + " is now at position " + finalPosition);

        // Step 7: Check win condition
        if (finalPosition == board.getSize()) {
            winner = currentPlayer;
            gameState = GameState.COMPLETED;
            System.out.println("\n*** " + winner.getName() + " wins the game! ***");
            return;
        }

        // Step 8: Give turn to next player
        moveToNextPlayer();
    }

    /**
     * Position 0 means player is not on board yet.
     */
    private int calculateNextPosition(int currentPosition, int diceValue) {
        // Player is still off the board
        if (currentPosition == 0) {
            return diceValue <= board.getSize() ? diceValue : 0;
        }

        // Exact finish rule: overshooting last cell means no move
        if (currentPosition + diceValue > board.getSize()) {
            return currentPosition;
        }

        return currentPosition + diceValue;
    }

    private void printJumperMessage(int landedPosition, int finalPosition) {
        if (landedPosition == finalPosition) {
            return;
        }

        Jumper jumper = board.getJumpers().get(landedPosition);
        if (jumper.getType() == JumperType.SNAKE) {
            System.out.println("Oops! Snake from " + landedPosition + " to " + finalPosition);
        } else {
            System.out.println("Great! Ladder from " + landedPosition + " to " + finalPosition);
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private void moveToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    private String formatPosition(int position) {
        return position == 0 ? "off-board" : String.valueOf(position);
    }

    private void validatePlayers(List<Player> players) {
        if (players == null || players.size() < MIN_PLAYERS || players.size() > MAX_PLAYERS) {
            throw new InvalidPlayerCountException("Game needs " + MIN_PLAYERS + " to " + MAX_PLAYERS + " players");
        }
    }
}
