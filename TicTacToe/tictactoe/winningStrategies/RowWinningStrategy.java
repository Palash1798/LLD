package com.palash.tictactoe.winningStrategies;

import com.palash.tictactoe.models.Board;
import com.palash.tictactoe.models.Cell;
import com.palash.tictactoe.models.Move;
import com.palash.tictactoe.models.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RowWinningStrategy extends MapWinningStrategy{

    private int dimension;
    private Map<Integer, Map<Player, Integer>> countMap;

    public RowWinningStrategy(int dimension, List<Player>players){
        super(dimension, players);
    }


    private  void initialiseCountMap(List<Player> playerList){
        for(int i=0; i < dimension; i++){
            countMap.put(i, new HashMap<>());
            for(Player player : playerList){
                countMap.get(i).put(player, 0);
            }
        }
    }
    @Override
    public void updateCount(Board board, Move lastMove) {
        Cell cell = lastMove.getCell();
        Player player = lastMove.getPlayer();
        int row = cell.getRow();

        updateCountMap(row, player);
    }

    @Override
    public boolean checkWinner(Board board, Move lastMove) {
        Cell cell = lastMove.getCell();
        Player player = lastMove.getPlayer();
        int row = cell.getRow();

        return checkCountMapForWinner(row, player, board.getSize());
    }

    @Override
    public void handleUndo(Board board, Move lastMove) {
        Player player = lastMove.getPlayer();
        Cell cell = lastMove.getCell();
        int row = cell.getRow();

        handleMapUndo(row, player);

    }
}
