package com.palash.tictactoe;

import com.palash.tictactoe.controller.GameController;
import com.palash.tictactoe.enums.BotDifficultyLevel;
import com.palash.tictactoe.enums.GameState;
import com.palash.tictactoe.enums.PlayerType;
import com.palash.tictactoe.exceptions.BotCountException;
import com.palash.tictactoe.exceptions.DimensionException;
import com.palash.tictactoe.exceptions.DuplicateSymbolException;
import com.palash.tictactoe.exceptions.PlayerCountException;
import com.palash.tictactoe.models.Game;
import com.palash.tictactoe.models.Player;
import com.palash.tictactoe.models.Bot;
import com.palash.tictactoe.models.Symbol;
import com.palash.tictactoe.winningStrategies.*;

import java.util.ArrayList;
import java.util.List;


public class TicTacToeGameMain {
    public static void main(String[] args) throws PlayerCountException, DuplicateSymbolException, BotCountException, DimensionException {
        GameController gameController = new GameController();

        int dimension = 3;

        List<Player> players = new ArrayList<>();
        players.add(new Player(1, "Palash", new Symbol('X')));
        players.add(new Player(2, "Akshay", new Symbol('B')));

        List<WinningStrategy> winningStrategies = new ArrayList<>();
        winningStrategies.add(new RowWinningStrategy(dimension, players));
        winningStrategies.add(new ColumnWinningStrategy(dimension, players));
        winningStrategies.add(new DiagonalWinningStrategy(players));
        winningStrategies.add(new CornersWinningStrategy());

        Game game = gameController.startGame(dimension, players, winningStrategies);
        System.out.println("Game has been Started...");

        gameController.displayBoard(game);
        while(game.getGameState() == GameState.IN_PROGRESS){
            gameController.makeMove(game);
            gameController.displayBoard(game);
            gameController.checkForUndo(game);
        }

        gameController.displayBoard(game);
        if(game.getGameState().equals(GameState.COMPLETED)){
            System.out.println("GAME OVER");
            System.out.println("Winner is " + game.getWinner().getName());
        }

        if(game.getGameState() == GameState.DRAW){
            System.out.println("Game is DRAW");
        }
    }
}


//            Needs to work on
//            1. Debug the bot functionality
//            2. Dimension and player details should not be hardcoded...allow user to add them with scanner.