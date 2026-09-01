package com.snakeandladder.snakeandladder.models;

import com.snakeandladder.snakeandladder.enums.JumperType;
import lombok.Getter;

/**
 * Ladder: player lands on bottom and climbs up to top.
 */
@Getter
public class Ladder implements Jumper {

    private final int bottom;
    private final int top;

    public Ladder(int bottom, int top) {
        this.bottom = bottom;
        this.top = top;
    }

    @Override
    public int getStart() {
        return bottom;
    }

    @Override
    public int getEnd() {
        return top;
    }

    @Override
    public JumperType getType() {
        return JumperType.LADDER;
    }
}
