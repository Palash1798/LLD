package com.cricbuzz.cricbuzz.services;

import com.cricbuzz.cricbuzz.models.Match;
import com.cricbuzz.cricbuzz.models.Team;
import com.cricbuzz.cricbuzz.strategies.MatchType;

import java.util.Date;

/**
 * Orchestrates match lifecycle — create, start, and print result.
 * Ball-by-ball rules live in {@link com.cricbuzz.cricbuzz.models.inning.BallDetails}.
 */
public class MatchService {

    public Match createMatch(Team teamA, Team teamB, String venue, MatchType matchType) {
        return new Match(teamA, teamB, new Date(), venue, matchType);
    }

    public void startMatch(Match match) {
        match.startMatch();
    }
}
