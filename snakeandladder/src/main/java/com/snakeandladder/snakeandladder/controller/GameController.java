package com.snakeandladder.snakeandladder.controller;

import com.snakeandladder.snakeandladder.enums.GameState;
import com.snakeandladder.snakeandladder.models.Board;
import com.snakeandladder.snakeandladder.models.Dice;
import com.snakeandladder.snakeandladder.models.Game;
import com.snakeandladder.snakeandladder.models.Player;

import java.util.List;

/**
 * Stateless controller.
 * It only forwards requests to the Game model.
 */
public class GameController {

    public Game startDefaultGame(List<Player> players) {
        return Game.createDefault(players);
    }

    public void playTurn(Game game) {
        game.playTurn();
    }

    public GameState getGameState(Game game) {
        return game.getGameState();
    }

    public Player getWinner(Game game) {
        return game.getWinner();
    }

    public void printPlayerPositions(Game game) {
        System.out.println("--- Player Positions ---");
        for (Player player : game.getPlayers()) {
            int position = player.getCurrentPosition();
            String positionText = position == 0 ? "off-board" : String.valueOf(position);
            System.out.println(player.getName() + " : " + positionText);
        }
    }
}
