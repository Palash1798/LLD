package com.cricbuzz.cricbuzz.models;

import com.cricbuzz.cricbuzz.enums.WicketType;
import com.cricbuzz.cricbuzz.models.inning.BallDetails;
import com.cricbuzz.cricbuzz.models.inning.OverDetails;
import com.cricbuzz.cricbuzz.models.player.PlayerDetails;

public class Wicket {

    public WicketType wicketType;
    public PlayerDetails takenBy;
    public OverDetails overDetail;
    public BallDetails ballDetail;

    public Wicket(WicketType wicketType, PlayerDetails takenBy, OverDetails overDetail, BallDetails ballDetail) {
        this.wicketType = wicketType;
        this.takenBy = takenBy;
        this.overDetail = overDetail;
        this.ballDetail = ballDetail;
    }
}
