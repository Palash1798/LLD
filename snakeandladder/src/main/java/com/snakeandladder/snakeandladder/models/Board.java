package com.snakeandladder.snakeandladder.models;

import com.snakeandladder.snakeandladder.enums.JumperType;
import com.snakeandladder.snakeandladder.exceptions.InvalidJumperException;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Board holds cells from 1 to size and all snakes/ladders.
 */
@Getter
public class Board {

    private final int size;
    private final Map<Integer, Jumper> jumpers = new HashMap<>();

    public Board(int size, List<Jumper> jumperList) {
        this.size = size;
        buildJumpers(jumperList);
    }

    /**
     * Step 1: Put every snake/ladder into a map using start cell as key.
     * Step 2: Validate so board setup is correct before game starts.
     */
    private void buildJumpers(List<Jumper> jumperList) {
        for (Jumper jumper : jumperList) {
            validateJumper(jumper);

            if (jumpers.containsKey(jumper.getStart())) {
                throw new InvalidJumperException(
                        "Two jumpers cannot start on the same cell: " + jumper.getStart());
            }

            jumpers.put(jumper.getStart(), jumper);
        }
    }

    private void validateJumper(Jumper jumper) {
        if (jumper.getStart() < 1 || jumper.getStart() > size
                || jumper.getEnd() < 1 || jumper.getEnd() > size) {
            throw new InvalidJumperException("Jumper cells must be between 1 and " + size);
        }

        if (jumper.getType() == JumperType.SNAKE && jumper.getStart() <= jumper.getEnd()) {
            throw new InvalidJumperException("Snake head must be greater than tail");
        }

        if (jumper.getType() == JumperType.LADDER && jumper.getStart() >= jumper.getEnd()) {
            throw new InvalidJumperException("Ladder bottom must be less than top");
        }
    }

    /**
     * If current cell has a snake/ladder, return the destination cell.
     * Otherwise return the same position.
     */
    public int getDestination(int position) {
        if (jumpers.containsKey(position)) {
            return jumpers.get(position).getEnd();
        }
        return position;
    }
}
