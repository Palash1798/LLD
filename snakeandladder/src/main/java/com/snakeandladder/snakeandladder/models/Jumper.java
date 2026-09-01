package com.snakeandladder.snakeandladder.models;

import com.snakeandladder.snakeandladder.enums.JumperType;

/**
 * Common contract for snakes and ladders.
 * Both move a player from one cell to another.
 */
public interface Jumper {

    int getStart();

    int getEnd();

    JumperType getType();
}
