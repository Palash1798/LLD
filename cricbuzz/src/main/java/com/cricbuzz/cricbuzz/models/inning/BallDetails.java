package com.cricbuzz.cricbuzz.models.inning;

import com.cricbuzz.cricbuzz.enums.BallType;
import com.cricbuzz.cricbuzz.enums.RunType;
import com.cricbuzz.cricbuzz.enums.WicketType;
import com.cricbuzz.cricbuzz.models.Team;
import com.cricbuzz.cricbuzz.models.Wicket;
import com.cricbuzz.cricbuzz.models.player.PlayerDetails;
import com.cricbuzz.cricbuzz.observers.BattingScoreUpdater;
import com.cricbuzz.cricbuzz.observers.BowlingScoreUpdater;
import com.cricbuzz.cricbuzz.observers.ScoreUpdaterObserver;

import java.util.ArrayList;
import java.util.List;

public class BallDetails {

    public int ballNumber;
    public BallType ballType;
    public RunType runType;
    public PlayerDetails playedBy;
    public PlayerDetails bowledBy;
    public Wicket wicket;

    private final List<ScoreUpdaterObserver> scoreUpdaterObserverList = new ArrayList<>();

    public BallDetails(int ballNumber) {
        this.ballNumber = ballNumber;
        scoreUpdaterObserverList.add(new BowlingScoreUpdater());
        scoreUpdaterObserverList.add(new BattingScoreUpdater());
    }

    public void addObserver(ScoreUpdaterObserver observer) {
        scoreUpdaterObserverList.add(observer);
    }

    public void startBallDelivery(Team battingTeam, Team bowlingTeam, OverDetails over) {
        playedBy = battingTeam.getStriker();
        this.bowledBy = over.bowledBy;
        ballType = BallType.NORMAL;

        if (isWicketTaken()) {
            runType = RunType.ZERO;
            wicket = new Wicket(WicketType.BOLD, bowlingTeam.getCurrentBowler(), over, this);
            battingTeam.setStriker(null);
        } else {
            runType = getRunType();

            if (runType == RunType.ONE || runType == RunType.THREE) {
                PlayerDetails temp = battingTeam.getStriker();
                battingTeam.setStriker(battingTeam.getNonStriker());
                battingTeam.setNonStriker(temp);
            }
        }

        notifyUpdaters(this);
    }

    private void notifyUpdaters(BallDetails ballDetails) {
        for (ScoreUpdaterObserver observer : scoreUpdaterObserverList) {
            observer.update(ballDetails);
        }
    }

    private RunType getRunType() {
        double val = Math.random();
        if (val <= 0.2) {
            return RunType.ONE;
        } else if (val >= 0.3 && val <= 0.5) {
            return RunType.TWO;
        } else if (val >= 0.6 && val <= 0.8) {
            return RunType.FOUR;
        } else {
            return RunType.SIX;
        }
    }

    private boolean isWicketTaken() {
        return Math.random() < 0.2;
    }
}
