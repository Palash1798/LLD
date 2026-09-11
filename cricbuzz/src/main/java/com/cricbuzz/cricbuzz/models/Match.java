package com.cricbuzz.cricbuzz.models;

import com.cricbuzz.cricbuzz.enums.MatchStatus;
import com.cricbuzz.cricbuzz.models.inning.InningDetails;
import com.cricbuzz.cricbuzz.strategies.MatchType;

import java.util.Date;

public class Match {

    private final Team teamA;
    private final Team teamB;
    private final Date matchDate;
    private final String venue;
    private final MatchType matchType;
    private Team tossWinner;
    private final InningDetails[] innings;
    private MatchStatus status;

    public Match(Team teamA, Team teamB, Date matchDate, String venue, MatchType matchType) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.matchDate = matchDate;
        this.venue = venue;
        this.matchType = matchType;
        this.innings = new InningDetails[2];
        this.status = MatchStatus.SCHEDULED;
    }

    public void startMatch() {
        status = MatchStatus.LIVE;
        tossWinner = toss(teamA, teamB);

        for (int inning = 1; inning <= 2; inning++) {
            InningDetails inningDetails;
            Team bowlingTeam;
            Team battingTeam;

            if (inning == 1) {
                battingTeam = tossWinner;
                bowlingTeam = tossWinner.getTeamName().equals(teamA.getTeamName()) ? teamB : teamA;
                inningDetails = new InningDetails(battingTeam, bowlingTeam, matchType);
                inningDetails.start(-1);
            } else {
                bowlingTeam = tossWinner;
                battingTeam = tossWinner.getTeamName().equals(teamA.getTeamName()) ? teamB : teamA;
                inningDetails = new InningDetails(battingTeam, bowlingTeam, matchType);
                inningDetails.start(innings[0].getTotalRuns());
                if (bowlingTeam.getTotalRuns() > battingTeam.getTotalRuns()) {
                    bowlingTeam.isWinner = true;
                }
            }

            innings[inning - 1] = inningDetails;
            printInningSummary(inning, battingTeam, bowlingTeam);
        }

        status = MatchStatus.COMPLETED;
        printWinner();
    }

    private void printInningSummary(int inning, Team battingTeam, Team bowlingTeam) {
        System.out.println();
        System.out.println("INNING " + inning + " -- total Run: " + battingTeam.getTotalRuns());
        System.out.println("---Batting ScoreCard : " + battingTeam.teamName + "---");
        battingTeam.printBattingScoreCard();

        System.out.println();
        System.out.println("---Bowling ScoreCard : " + bowlingTeam.teamName + "---");
        bowlingTeam.printBowlingScoreCard();
    }

    private void printWinner() {
        System.out.println();
        if (teamA.isWinner) {
            System.out.println("---WINNER---" + teamA.teamName);
        } else {
            System.out.println("---WINNER---" + teamB.teamName);
        }
    }

    private Team toss(Team first, Team second) {
        return Math.random() < 0.5 ? first : second;
    }

    public Team getTeamA() {
        return teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public String getVenue() {
        return venue;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public InningDetails[] getInnings() {
        return innings;
    }
}
