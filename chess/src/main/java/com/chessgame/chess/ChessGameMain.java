package com.chessgame.chess;

import com.chessgame.chess.controller.GameController;
import com.chessgame.chess.enums.GameState;
import com.chessgame.chess.exceptions.GameAlreadyEndedException;
import com.chessgame.chess.exceptions.InvalidMoveException;
import com.chessgame.chess.exceptions.WrongTurnException;
import com.chessgame.chess.model.Game;

import java.util.Scanner;

/**
 * Simple console runner for interview demos.
 * Run this class (not Spring Boot) to play:
 *
 *   Input format: e2 e4
 *   Commands:     resign | quit | help
 */
public class ChessGameMain {

    public static void main(String[] args) {
        // Step 1: wire controller + start game
        GameController controller = new GameController();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Chess LLD Demo ===");
        System.out.print("White player name: ");
        String whiteName = readNonEmpty(scanner, "Alice");

        System.out.print("Black player name: ");
        String blackName = readNonEmpty(scanner, "Bob");

        Game game = controller.startGame(whiteName, blackName);

        // Step 2: show starting board
        controller.displayBoard(game);
        printHelp();

        // Step 3: game loop until completed / quit
        while (controller.getGameState(game) == GameState.IN_PROGRESS) {
            System.out.print(controller.getCurrentPlayer(game) + " move (e.g. e2 e4): ");

            if (!scanner.hasNextLine()) {
                break;
            }

            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            // Step 4: handle special commands
            if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                System.out.println("Exiting without winner.");
                break;
            }
            if (line.equalsIgnoreCase("help")) {
                printHelp();
                continue;
            }
            if (line.equalsIgnoreCase("resign")) {
                controller.resign(game, controller.getCurrentPlayer(game));
                break;
            }

            // Step 5: parse "e2 e4"
            String[] parts = line.split("\\s+");
            if (parts.length != 2) {
                System.out.println("Invalid input. Use: e2 e4");
                continue;
            }

            // Step 6: try the move; print friendly errors
            try {
                controller.makeMove(game, parts[0], parts[1]);
                controller.displayBoard(game);
            } catch (InvalidMoveException | WrongTurnException | GameAlreadyEndedException
                     | IllegalArgumentException ex) {
                System.out.println("✗ " + ex.getMessage());
            }
        }

        // Step 7: final result
        if (controller.getWinner(game) != null) {
            System.out.println("Winner: " + controller.getWinner(game));
        }
        System.out.println("Game state: " + controller.getGameState(game));
        scanner.close();
    }

    private static String readNonEmpty(Scanner scanner, String defaultName) {
        String value = scanner.nextLine().trim();
        return value.isEmpty() ? defaultName : value;
    }

    private static void printHelp() {
        System.out.println("""
                Commands:
                  e2 e4   → move piece from e2 to e4
                  resign  → current player resigns
                  help    → show this help
                  quit    → exit
                """);
    }
}
