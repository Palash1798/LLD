package com.snakeandladder.snakeandladder;

import com.snakeandladder.snakeandladder.controller.GameController;
import com.snakeandladder.snakeandladder.enums.GameState;
import com.snakeandladder.snakeandladder.models.Game;
import com.snakeandladder.snakeandladder.models.Player;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point to run the game from command line.
 */
@SpringBootApplication
public class SnakeandladderApplication {

    public static void main(String[] args) {
        GameController gameController = new GameController();

        // Step 1: Create players
        List<Player> players = new ArrayList<>();
        players.add(new Player(1, "Alice"));
        players.add(new Player(2, "Bob"));

        // Step 2: Start game with default 100-cell board
        Game game = gameController.startDefaultGame(players);
        System.out.println("Snake and Ladder game started!");
        gameController.printPlayerPositions(game);

        // Step 3: Keep playing until someone wins
        while (gameController.getGameState(game) == GameState.IN_PROGRESS) {
            gameController.playTurn(game);
            gameController.printPlayerPositions(game);
        }

        // Step 4: Print final result
        System.out.println("Winner: " + gameController.getWinner(game).getName());
    }

}
