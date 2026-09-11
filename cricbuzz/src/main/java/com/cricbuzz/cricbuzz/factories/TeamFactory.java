package com.cricbuzz.cricbuzz.factories;

import com.cricbuzz.cricbuzz.enums.PlayerType;
import com.cricbuzz.cricbuzz.models.Team;
import com.cricbuzz.cricbuzz.models.player.Person;
import com.cricbuzz.cricbuzz.models.player.PlayerDetails;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class TeamFactory {

    private TeamFactory() {
    }

    public static Team createTeam(String name) {
        Queue<PlayerDetails> playing11 = new LinkedList<>();

        for (int i = 1; i <= 11; i++) {
            playing11.add(createPlayer(name + i, PlayerType.ALLROUNDER));
        }

        List<PlayerDetails> allPlayers = new ArrayList<>(playing11);
        List<PlayerDetails> bowlers = new ArrayList<>(allPlayers.subList(7, 11));

        return new Team(name, playing11, new ArrayList<>(), bowlers);
    }

    private static PlayerDetails createPlayer(String name, PlayerType playerType) {
        Person person = new Person();
        person.name = name;
        return new PlayerDetails(person, playerType);
    }
}
