package com.cricbuzz.cricbuzz.models.inning;

import com.cricbuzz.cricbuzz.models.Team;
import com.cricbuzz.cricbuzz.models.player.PlayerDetails;
import com.cricbuzz.cricbuzz.strategies.MatchType;

import java.util.ArrayList;
import java.util.List;

public class InningDetails {

    Team battingTeam;
    Team bowlingTeam;
    MatchType matchType;
    List<OverDetails> overs;

    public InningDetails(Team battingTeam, Team bowlingTeam, MatchType matchType) {
        this.battingTeam = battingTeam;
        this.bowlingTeam = bowlingTeam;
        this.matchType = matchType;
        overs = new ArrayList<>();
    }

    public void start(int runsToWin) {
        try {
            battingTeam.chooseNextBatsMan();
        } catch (Exception ignored) {
            // all out before innings starts — rare edge case in random sim
        }

        int noOfOvers = matchType.noOfOvers();
        for (int overNumber = 1; overNumber <= noOfOvers; overNumber++) {
            bowlingTeam.chooseNextBowler(matchType.maxOverCountBowlers());

            OverDetails over = new OverDetails(overNumber, bowlingTeam.getCurrentBowler());
            overs.add(over);
            try {
                boolean won = over.startOver(battingTeam, bowlingTeam, runsToWin);
                if (won) {
                    break;
                }
            } catch (Exception e) {
                break;
            }

            PlayerDetails temp = battingTeam.getStriker();
            battingTeam.setStriker(battingTeam.getNonStriker());
            battingTeam.setNonStriker(temp);
        }
    }

    public int getTotalRuns() {
        return battingTeam.getTotalRuns();
    }

    public Team getBattingTeam() {
        return battingTeam;
    }

    public Team getBowlingTeam() {
        return bowlingTeam;
    }
}
