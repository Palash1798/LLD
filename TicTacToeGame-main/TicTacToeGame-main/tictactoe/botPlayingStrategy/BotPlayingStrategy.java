package com.palash.tictactoe.botPlayingStrategy;

import com.palash.tictactoe.models.Board;
import com.palash.tictactoe.models.Move;

public interface BotPlayingStrategy {
    Move makeBotMove(Board board);
}
