package com.palash.tictactoe.models;

import com.palash.tictactoe.enums.CellState;
import com.palash.tictactoe.enums.GameState;
import com.palash.tictactoe.enums.PlayerType;
import com.palash.tictactoe.exceptions.BotCountException;
import com.palash.tictactoe.exceptions.DimensionException;
import com.palash.tictactoe.exceptions.DuplicateSymbolException;
import com.palash.tictactoe.exceptions.PlayerCountException;
import com.palash.tictactoe.winningStrategies.WinningStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Game {
    private Board board;
    private List<Player> player;
    private GameState gameState;
    private Player winner;
    private List<Move> moves;
    private int currentPlayerIndex;
    private List<WinningStrategy> winningStrategies;

    private Game(Builder builder){
        this.player = builder.players;
        this.winningStrategies = builder.winningStrategies;
        this.board = new Board(builder.dimension);

        this.gameState = GameState.IN_PROGRESS;
        this.winner = null;
        this.moves = new ArrayList<>();
        this.currentPlayerIndex = 0;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Player> getPlayer() {
        return player;
    }

    public void setPlayer(List<Player> player) {
        this.player = player;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public List<WinningStrategy> getWinningStrategies() {
        return winningStrategies;
    }

    public void setWinningStrategies(List<WinningStrategy> winningStrategies) {
        this.winningStrategies = winningStrategies;
    }



    public void makeMove(){
        int currentPlayerIndex = this.getCurrentPlayerIndex();
        Player currentPlayer = this.getPlayer().get(currentPlayerIndex);

        Move move = currentPlayer.makeMove(this.getBoard());
        this.moves.add(move);

        // update the countMaps
        for(WinningStrategy winningStrategy: this.winningStrategies){
            winningStrategy.updateCount(this.getBoard(), move);

            if(winningStrategy.checkWinner(board, move)){
                this.setWinner(currentPlayer);
                this.setGameState(GameState.COMPLETED);
                return;
            }
        }

        if(this.moves.size() == board.getSize() * board.getSize()){
            this.setGameState(GameState.DRAW);
            return;
        }

        int nextPlayerIndex = (currentPlayerIndex + 1) % (this.getPlayer().size());
        this.setCurrentPlayerIndex(nextPlayerIndex);
    }

    public void checkForUndo(){
        //if last move is played by bot...it will not ask for undo.
        int movesSize = this.moves.size();
        Move lastMove = this.moves.get(movesSize-1);
        if(lastMove.getPlayer().getPlayerType().equals(PlayerType.BOT)){
            return;
        }

        //Human player will aks for undo.
        System.out.println("Do you want to undo : ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();

        if(input.equals("Y")){
            performUndo();
        }
    }

    public void performUndo(){
        // update moves list
        int movesSize = this.moves.size();
        Move lastMove = this.moves.get(movesSize-1);
        this.moves.remove(movesSize-1);


        // reset the player index
        int currentIndex = this.currentPlayerIndex;
        int newIndex = currentIndex != 0 ? currentIndex - 1 : player.size()-1;
        this.setCurrentPlayerIndex(newIndex);

        // reset the cell

        lastMove.getCell().setPlayer(null);
        lastMove.getCell().setCellState(CellState.EMPTY);

        // decrement the countMap
        for(WinningStrategy winningStrategy: getWinningStrategies()){
            winningStrategy.handleUndo(board, lastMove);
        }
    }

    public static Builder getBuilder(){
        return new Builder();
    }

    public static class Builder{
        private int dimension;
        private List<Player> players;
        private List<WinningStrategy> winningStrategies;

        public Builder setDimension(int dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder setPlayers(List<Player> players) {
            this.players = players;
            return this;
        }

        public Builder setWinningStrategies(List<WinningStrategy> winningStrategies) {
            this.winningStrategies = winningStrategies;
            return this;
        }

        private void validateBotCount() throws BotCountException{
            //No. of bot should be one.
            int botCount = 0;
            for(Player p : players){
                if(p.getPlayerType() == PlayerType.BOT){
                    botCount++;
                }
            }
            if(botCount > 1){
                throw new BotCountException("Bot count should not be more than 1");
            }
        }

        private void validateBoardSize() throws DimensionException {
            //Size of board should not be less than 3.
            if(dimension < 3){
                throw new DimensionException("Board size should not be less than 3");
            }
        }

        private void validatePlayerCount() throws PlayerCountException{
            //No. of player is N-1.
            if(players.size() != dimension-1){
                throw new PlayerCountException("Player count shold be N-1");
            }
        }

        private void validateDuplicateSymbols() throws DuplicateSymbolException {
            //No duplicate Symbols.
            HashSet<Character> symbols = new HashSet<>();
            for(Player p : players){
                if(symbols.contains(p.getSymbol().getaChar())){
                    throw new DuplicateSymbolException("Duplicate Player Symbol Found! Please try again with new Symbols.");
                }
                symbols.add(p.getSymbol().getaChar());
            }
        }

//        This method is violating the SRP...therefor we wrote above 4 different method for exception handling
//        and call the function here
           private void validate() throws BotCountException, DimensionException, PlayerCountException, DuplicateSymbolException {
                validateBotCount();
                validateBoardSize();
                validatePlayerCount();
                validateDuplicateSymbols();
           }
        public Game build() throws PlayerCountException, DuplicateSymbolException, BotCountException, DimensionException {
            //Validation.
            validate();
            return new Game(this);
        }
    }
}
