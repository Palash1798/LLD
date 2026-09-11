package com.cricbuzz.cricbuzz.strategies;

public class OneDayMatchType implements MatchType {

    @Override
    public int noOfOvers() {
        return 50;
    }

    @Override
    public int maxOverCountBowlers() {
        return 10;
    }
}
