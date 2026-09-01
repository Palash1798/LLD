package com.palash.tictactoe.botPlayingStrategy;

import com.palash.tictactoe.enums.CellState;
import com.palash.tictactoe.models.Board;
import com.palash.tictactoe.models.Bot;
import com.palash.tictactoe.models.Cell;
import com.palash.tictactoe.models.Move;

import java.util.ArrayList;
import java.util.List;

public class EasyBotPlayingStrategy implements BotPlayingStrategy{
    Bot bot;

    public EasyBotPlayingStrategy(Bot bot){
        this.bot = bot;
    }
    @Override
    public Move makeBotMove(Board board) {
        System.out.println("It's " + bot.getName() + "'s turn. Thinking...");
        List<Cell> emptyCellsList = getEmptyCells(board);

        Cell choosenCell = emptyCellsList.get(0);

        choosenCell.setPlayer(bot);
        choosenCell.setCellState(CellState.OCCUPIED);
        return new Move(choosenCell, bot);
    }

    public List<Cell> getEmptyCells(Board board){
        int sizeOfBoard = board.getSize();
        List<Cell> emptyCells = new ArrayList<>();
        for(int i=0; i < sizeOfBoard; i++){
            for(int j=0; j < sizeOfBoard; j++){
                Cell currentCell = board.getBoard().get(i).get(j);
                if(currentCell.getCellState().equals(CellState.EMPTY));{
                    emptyCells.add(currentCell);
                }
            }
        }
        return emptyCells;
    }
}
