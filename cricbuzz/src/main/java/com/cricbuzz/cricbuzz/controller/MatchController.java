package com.cricbuzz.cricbuzz.controller;

import com.cricbuzz.cricbuzz.models.Match;
import com.cricbuzz.cricbuzz.models.Team;
import com.cricbuzz.cricbuzz.services.MatchService;
import com.cricbuzz.cricbuzz.strategies.MatchType;

/**
 * Thin controller (same idea as AtmController / BookingController).
 * Keeps CLI layer free of ball-by-ball cricket rules.
 */
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    /** Feature 1: create match with two teams and format strategy. */
    public Match createMatch(Team teamA, Team teamB, String venue, MatchType matchType) {
        return matchService.createMatch(teamA, teamB, venue, matchType);
    }

    /** Features 2+3: run full simulated match (ball-by-ball + scorecards). */
    public void startMatch(Match match) {
        matchService.startMatch(match);
    }
}
