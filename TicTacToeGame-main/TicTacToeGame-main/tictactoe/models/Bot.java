package com.palash.tictactoe.models;

import com.palash.tictactoe.botPlayingStrategy.BotPlayingStrategy;
import com.palash.tictactoe.enums.BotDifficultyLevel;
import com.palash.tictactoe.enums.PlayerType;
import com.palash.tictactoe.factories.BotPlayingStrategyFactory;

public class Bot extends Player{
    private BotDifficultyLevel difficultyLevel;

    public Bot(int id, String name, Symbol symbol, BotDifficultyLevel difficultyLevel) {
        super(id, name, symbol);
        this.difficultyLevel = difficultyLevel;
        this.setPlayerType(PlayerType.BOT);
    }

    public BotDifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(BotDifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    @Override
    public Move makeMove(Board board){
        BotPlayingStrategy playingStrategy = BotPlayingStrategyFactory.getBotPlayingStrategyByDifficultyLevel(this);
        return playingStrategy.makeBotMove(board);
    }
}