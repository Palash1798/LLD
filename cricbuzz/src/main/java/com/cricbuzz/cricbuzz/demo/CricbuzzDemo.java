package com.cricbuzz.cricbuzz.demo;

import com.cricbuzz.cricbuzz.controller.MatchController;
import com.cricbuzz.cricbuzz.factories.TeamFactory;
import com.cricbuzz.cricbuzz.models.Match;
import com.cricbuzz.cricbuzz.models.Team;
import com.cricbuzz.cricbuzz.services.MatchService;
import com.cricbuzz.cricbuzz.strategies.T20MatchType;

/**
 * Interactive entry — same wiring as {@link com.cricbuzz.cricbuzz.CricbuzzApplication}
 * but you can swap teams/format here while practicing.
 */
public class CricbuzzDemo {

    public static void main(String[] args) {
        MatchController controller = new MatchController(new MatchService());

        Team india = TeamFactory.createTeam("India");
        Team sriLanka = TeamFactory.createTeam("SriLanka");

        Match match = controller.createMatch(india, sriLanka, "SMS STADIUM", new T20MatchType());
        controller.startMatch(match);
    }
}
