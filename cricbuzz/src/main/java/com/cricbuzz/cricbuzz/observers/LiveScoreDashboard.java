package com.cricbuzz.cricbuzz.observers;

import com.cricbuzz.cricbuzz.enums.RunType;
import com.cricbuzz.cricbuzz.models.Team;
import com.cricbuzz.cricbuzz.models.inning.BallDetails;

/**
 * Prints a one-line live ticker after each ball (Observer for demo/interview).
 */
public class LiveScoreDashboard implements ScoreUpdaterObserver {

    private final Team battingTeam;

    public LiveScoreDashboard(Team battingTeam) {
        this.battingTeam = battingTeam;
    }

    @Override
    public void update(BallDetails ballDetails) {
        String outcome = ballDetails.wicket != null
                ? "WICKET"
                : ballDetails.runType.name();
        System.out.println("[LIVE] " + battingTeam.getTeamName() + " "
                + battingTeam.getTotalRuns() + " | ball " + ballDetails.ballNumber
                + " → " + outcome
                + (ballDetails.runType != RunType.ZERO && ballDetails.wicket == null
                ? " (" + runsFromType(ballDetails.runType) + " runs)" : ""));
    }

    private int runsFromType(RunType runType) {
        return switch (runType) {
            case ONE -> 1;
            case TWO -> 2;
            case THREE -> 3;
            case FOUR -> 4;
            case SIX -> 6;
            default -> 0;
        };
    }
}
