package com.snakeandladder.snakeandladder;

import com.snakeandladder.snakeandladder.controller.GameController;
import com.snakeandladder.snakeandladder.enums.GameState;
import com.snakeandladder.snakeandladder.models.Game;
import com.snakeandladder.snakeandladder.models.Player;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point to run the game from command line.
 */
@SpringBootApplication
public class SnakeandladderApplication {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameController gameController = new GameController();

        System.out.println("=== Welcome to Snake and Ladder ===\n");

        // Step 1: Ask user how many players will play
        List<Player> players = readPlayersFromUser(scanner);

        // Step 2: Start game with default 100-cell board
        Game game = gameController.startDefaultGame(players);
        System.out.println("\nGame started! First player to reach cell 100 wins.");
        gameController.printPlayerPositions(game);

        // Step 3: Play turn by turn — wait for user input before each roll
        while (gameController.getGameState(game) == GameState.IN_PROGRESS) {
            Player currentPlayer = game.getCurrentPlayer();

            System.out.println("\n----------------------------------------");
            System.out.println(">>> " + currentPlayer.getName() + "'s turn <<<");
            System.out.print("Press Enter to roll the dice...");
            scanner.nextLine();

            gameController.playTurn(game);
            gameController.printPlayerPositions(game);
        }

        // Step 4: Print final result
        System.out.println("\n========================================");
        System.out.println("Winner: " + gameController.getWinner(game).getName());
        System.out.println("========================================");

        scanner.close();
    }

    /**
     * Reads player count and names from the console.
     */
    private static List<Player> readPlayersFromUser(Scanner scanner) {
        int playerCount = readPlayerCount(scanner);

        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= playerCount; i++) {
            System.out.print("Enter name for Player " + i + ": ");
            String name = scanner.nextLine().trim();

            // Use a default name if user presses Enter without typing
            if (name.isEmpty()) {
                name = "Player" + i;
            }

            players.add(new Player(i, name));
        }

        return players;
    }

    /**
     * Keeps asking until user enters a valid player count (2 to 4).
     */
    private static int readPlayerCount(Scanner scanner) {
        while (true) {
            System.out.print("Enter number of players (" + MIN_PLAYERS + "-" + MAX_PLAYERS + "): ");

            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                System.out.println("Please enter a valid number.");
                continue;
            }

            int count = scanner.nextInt();
            scanner.nextLine(); // consume leftover newline after nextInt()

            if (count >= MIN_PLAYERS && count <= MAX_PLAYERS) {
                return count;
            }

            System.out.println("Player count must be between " + MIN_PLAYERS + " and " + MAX_PLAYERS + ".");
        }
    }
}
