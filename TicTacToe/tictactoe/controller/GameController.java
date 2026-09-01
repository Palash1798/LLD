package com.palash.tictactoe.controller;

import com.palash.tictactoe.exceptions.BotCountException;
import com.palash.tictactoe.exceptions.DimensionException;
import com.palash.tictactoe.exceptions.DuplicateSymbolException;
import com.palash.tictactoe.exceptions.PlayerCountException;
import com.palash.tictactoe.models.Game;
import com.palash.tictactoe.models.Player;
import com.palash.tictactoe.winningStrategies.WinningStrategy;

import java.util.List;


// controller's responsibility is to just forward the request to the appropriate logic -> model, services
public class GameController {   //Controller is a stateless class.
    public Game startGame(int dimension, List<Player> players, List<WinningStrategy> winningStrategies) throws PlayerCountException, DuplicateSymbolException, BotCountException, DimensionException {
        return Game.getBuilder()
                .setDimension(dimension)
                .setPlayers(players)
                .setWinningStrategies(winningStrategies)
                .build();
    }
    public void displayBoard(Game game){
        game.getBoard().displayBoard();
    }
    public void makeMove(Game game){
        game.makeMove();
    }
    public void checkWinner(Game game){

    }

    public void checkForUndo(Game game){
       game.checkForUndo();
    }


}
