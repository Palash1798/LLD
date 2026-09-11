package com.cricbuzz.cricbuzz.models.player;

import com.cricbuzz.cricbuzz.enums.PlayerType;
import com.cricbuzz.cricbuzz.models.score.BattingScoreCard;
import com.cricbuzz.cricbuzz.models.score.BowlingScoreCard;

public class PlayerDetails {

    public Person person;
    public PlayerType playerType;
    public BattingScoreCard battingScoreCard;
    public BowlingScoreCard bowlingScoreCard;

    public PlayerDetails(Person person, PlayerType playerType) {
        this.person = person;
        this.playerType = playerType;
        battingScoreCard = new BattingScoreCard();
        bowlingScoreCard = new BowlingScoreCard();
    }

    public void printBattingScoreCard() {
        String outBy = battingScoreCard.wicketDetails != null
                ? battingScoreCard.wicketDetails.takenBy.person.name
                : "notout";
        System.out.println("PlayerName: " + person.name
                + " -- totalRuns: " + battingScoreCard.totalRuns
                + " -- totalBallsPlayed: " + battingScoreCard.totalBallsPlayed
                + " -- 4s: " + battingScoreCard.totalFours
                + " -- 6s: " + battingScoreCard.totalSix
                + " -- outby: " + outBy);
    }

    public void printBowlingScoreCard() {
        System.out.println("PlayerName: " + person.name
                + " -- totalOversThrown: " + bowlingScoreCard.totalOversCount
                + " -- totalRunsGiven: " + bowlingScoreCard.runsGiven
                + " -- WicketsTaken: " + bowlingScoreCard.wicketsTaken);
    }
}
