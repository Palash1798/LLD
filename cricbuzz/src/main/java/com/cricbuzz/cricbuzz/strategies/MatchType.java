package com.cricbuzz.cricbuzz.strategies;

/**
 * Strategy for match format rules (T20 vs ODI).
 */
public interface MatchType {
    int noOfOvers();

    int maxOverCountBowlers();
}
