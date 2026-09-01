package com.snakeandladder.snakeandladder.models;

import com.snakeandladder.snakeandladder.enums.JumperType;
import lombok.Getter;

/**
 * Snake: player lands on head and slides down to tail.
 */
@Getter
public class Snake implements Jumper {

    private final int head;
    private final int tail;

    public Snake(int head, int tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public int getStart() {
        return head;
    }

    @Override
    public int getEnd() {
        return tail;
    }

    @Override
    public JumperType getType() {
        return JumperType.SNAKE;
    }
}
