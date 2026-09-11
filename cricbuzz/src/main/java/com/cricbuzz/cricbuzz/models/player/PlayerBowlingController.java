package com.cricbuzz.cricbuzz.models.player;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PlayerBowlingController {

    Deque<PlayerDetails> bowlersList;
    Map<PlayerDetails, Integer> bowlerVsOverCount;
    PlayerDetails currentBowler;

    public PlayerBowlingController(List<PlayerDetails> bowlersList) {
        setBowlersList(bowlersList);
    }

    private void setBowlersList(List<PlayerDetails> bowlersList) {
        this.bowlersList = new LinkedList<>();
        bowlerVsOverCount = new HashMap<>();
        for (PlayerDetails bowler : bowlersList) {
            this.bowlersList.addLast(bowler);
            bowlerVsOverCount.put(bowler, 0);
        }
    }

    public void getNextBowler(int maxOverCountPerBowler) {
        PlayerDetails playerDetails = bowlersList.poll();
        currentBowler = playerDetails;
        if (bowlerVsOverCount.get(playerDetails) + 1 < maxOverCountPerBowler) {
            bowlersList.addLast(playerDetails);
            bowlerVsOverCount.put(playerDetails, bowlerVsOverCount.get(playerDetails) + 1);
        }
    }

    public PlayerDetails getCurrentBowler() {
        return currentBowler;
    }
}
