package com.cricbuzz.cricbuzz.observers;

import com.cricbuzz.cricbuzz.models.inning.BallDetails;

/**
 * Observer — notified after each ball so batting/bowling stats stay in sync.
 */
public interface ScoreUpdaterObserver {

    void update(BallDetails ballDetails);
}
