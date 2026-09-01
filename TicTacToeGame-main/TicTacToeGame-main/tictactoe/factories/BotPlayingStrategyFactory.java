package com.palash.tictactoe.factories;

import com.palash.tictactoe.botPlayingStrategy.BotPlayingStrategy;
import com.palash.tictactoe.botPlayingStrategy.EasyBotPlayingStrategy;
import com.palash.tictactoe.botPlayingStrategy.HardBotPlayingStrategy;
import com.palash.tictactoe.botPlayingStrategy.MediumBotPlayingStrategy;
import com.palash.tictactoe.enums.BotDifficultyLevel;
import com.palash.tictactoe.models.Bot;

public class BotPlayingStrategyFactory {
    public static BotPlayingStrategy getBotPlayingStrategyByDifficultyLevel(Bot bot){
        BotDifficultyLevel difficultyLevel = bot.getDifficultyLevel();
        if(difficultyLevel == BotDifficultyLevel.EASY){
            return new EasyBotPlayingStrategy(bot);
        } else if(difficultyLevel == BotDifficultyLevel.MEDIUM){
            return new MediumBotPlayingStrategy(bot);
        } else if(difficultyLevel == BotDifficultyLevel.HARD){
            return new HardBotPlayingStrategy(bot);
        }
        return null;
    }
}
