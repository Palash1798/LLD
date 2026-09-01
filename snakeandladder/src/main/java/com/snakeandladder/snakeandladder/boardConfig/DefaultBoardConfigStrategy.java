package com.snakeandladder.snakeandladder.boardConfig;

import com.snakeandladder.snakeandladder.models.Jumper;
import com.snakeandladder.snakeandladder.models.Ladder;
import com.snakeandladder.snakeandladder.models.Snake;

import java.util.ArrayList;
import java.util.List;

/**
 * Classic 100-cell snake and ladder layout.
 */
public class DefaultBoardConfigStrategy {

    public List<Jumper> getJumpers() {
        List<Jumper> jumpers = new ArrayList<>();

        // Snakes: head -> tail
        jumpers.add(new Snake(16, 6));
        jumpers.add(new Snake(47, 26));
        jumpers.add(new Snake(49, 11));
        jumpers.add(new Snake(56, 53));
        jumpers.add(new Snake(62, 19));
        jumpers.add(new Snake(64, 60));
        jumpers.add(new Snake(87, 24));
        jumpers.add(new Snake(93, 73));
        jumpers.add(new Snake(95, 75));
        jumpers.add(new Snake(98, 78));

        // Ladders: bottom -> top
        jumpers.add(new Ladder(1, 38));
        jumpers.add(new Ladder(4, 14));
        jumpers.add(new Ladder(9, 31));
        jumpers.add(new Ladder(21, 42));
        jumpers.add(new Ladder(28, 84));
        jumpers.add(new Ladder(36, 44));
        jumpers.add(new Ladder(51, 67));
        jumpers.add(new Ladder(71, 91));
        jumpers.add(new Ladder(80, 100));

        return jumpers;
    }
}
