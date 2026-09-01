package com.palash.tictactoe.winningStrategies;

import com.palash.tictactoe.models.Board;
import com.palash.tictactoe.models.Move;

public interface WinningStrategy {
    void updateCount(Board board, Move lastMove);
    boolean checkWinner(Board board, Move lastMove);
    void handleUndo(Board board, Move lastMove);

}
