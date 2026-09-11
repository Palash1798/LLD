package com.cricbuzz.cricbuzz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cricbuzz.cricbuzz.controller.MatchController;
import com.cricbuzz.cricbuzz.factories.TeamFactory;
import com.cricbuzz.cricbuzz.models.Match;
import com.cricbuzz.cricbuzz.models.Team;
import com.cricbuzz.cricbuzz.services.MatchService;
import com.cricbuzz.cricbuzz.strategies.OneDayMatchType;
import com.cricbuzz.cricbuzz.strategies.T20MatchType;

/**
 * Entry point — scripted study demo (mirrors AtmApplication).
 *
 * Walks through the 3 MVP features from LLD_CRICBUZZ.md:
 *   1) Start match (teams + format strategy)
 *   2) Ball-by-ball scoring (Observer updates batting/bowling stats)
 *   3) Over/innings scorecard + match result
 *
 * IDE: run this class, or CricbuzzDemo for a single quick match.
 */
@SpringBootApplication
public class CricbuzzApplication {

    public static void main(String[] args) {
        if (args.length == 0) {
            runStudyDemo();
            return;
        }
        SpringApplication.run(CricbuzzApplication.class, args);
    }

    private static void runStudyDemo() {
        System.out.println("========================================");
        System.out.println("  CRICBUZZ LLD — STUDY DEMO");
        System.out.println("  Patterns: Observer + Strategy (MatchType)");
        System.out.println("========================================\n");

        MatchController controller = new MatchController(new MatchService());

        section("FEATURE 1+2+3 — T20 match (India vs SriLanka)");
        Team india = TeamFactory.createTeam("India");
        Team sriLanka = TeamFactory.createTeam("SriLanka");
        Match t20Match = controller.createMatch(india, sriLanka, "Wankhede Stadium", new T20MatchType());
        controller.startMatch(t20Match);

        section("BONUS — ODI format via Strategy (50 overs, 10 overs max/bowler)");
        Team australia = TeamFactory.createTeam("Australia");
        Team england = TeamFactory.createTeam("England");
        Match odiMatch = controller.createMatch(australia, england, "Lord's", new OneDayMatchType());
        System.out.println("(Skipping full 50-over sim in demo — format strategy is wired.)");
        System.out.println("Max overs: " + odiMatch.getMatchType().noOfOvers());
        System.out.println("Max overs per bowler: " + odiMatch.getMatchType().maxOverCountBowlers());

        System.out.println("\nDemo complete. Re-read observers/ + models/inning/ — that is the heart of this LLD.");
        System.out.println("For a single match run: com.cricbuzz.cricbuzz.demo.CricbuzzDemo");
    }

    static void section(String title) {
        System.out.println("\n>>> " + title);
        System.out.println("------------------------------------------------");
    }
}
