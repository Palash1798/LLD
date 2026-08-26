package com.elevatorsystem.elevator.strategy;

import com.elevatorsystem.elevator.enums.Direction;
import com.elevatorsystem.elevator.model.Elevator;

import java.util.TreeSet;

/**
 * LOOK scheduling (Feature #2 — most important interview algorithm).
 *
 * Steps (revision cheat-sheet):
 * 1) While moving UP and upStops remain → keep UP, next = lowest upStop (TreeSet.first()).
 * 2) While moving DOWN and downStops remain → keep DOWN, next = highest downStop (TreeSet.last()).
 * 3) If current direction has no more stops → reverse if opposite set has work.
 * 4) If neither set has work → IDLE.
 * 5) Unlike SCAN, we NEVER travel past the last request to shaft extremes.
 */
public class LookStrategy implements SchedulingStrategy {

    @Override
    public Integer nextStop(Elevator elevator) {
        Direction direction = elevator.getDirection();
        TreeSet<Integer> upStops = elevator.getUpStops();
        TreeSet<Integer> downStops = elevator.getDownStops();

        // Step A: pick next stop in current travel direction
        if (direction == Direction.UP && !upStops.isEmpty()) {
            return upStops.first(); // lowest pending for UP set
        }
        if (direction == Direction.DOWN && !downStops.isEmpty()) {
            return downStops.last(); // highest pending for DOWN set
        }

        // Step B: IDLE or emptied current side → nearest pending in either set
        if (!upStops.isEmpty() && !downStops.isEmpty()) {
            int up = upStops.first();
            int down = downStops.last();
            int floor = elevator.getCurrentFloor();
            return Math.abs(up - floor) <= Math.abs(down - floor) ? up : down;
        }
        if (!upStops.isEmpty()) {
            return upStops.first();
        }
        if (!downStops.isEmpty()) {
            return downStops.last();
        }
        return null;
    }

    @Override
    public Direction resolveDirection(Elevator elevator) {
        TreeSet<Integer> upStops = elevator.getUpStops();
        TreeSet<Integer> downStops = elevator.getDownStops();
        Direction current = elevator.getDirection();

        // Step 1: no work → IDLE
        if (upStops.isEmpty() && downStops.isEmpty()) {
            return Direction.IDLE;
        }

        // Step 2: continue in same direction if that side still has stops
        if (current == Direction.UP && !upStops.isEmpty()) {
            return Direction.UP;
        }
        if (current == Direction.DOWN && !downStops.isEmpty()) {
            return Direction.DOWN;
        }

        // Step 3: LOOK reverse — current side empty, opposite side has work
        if (current == Direction.UP && !downStops.isEmpty()) {
            return Direction.DOWN;
        }
        if (current == Direction.DOWN && !upStops.isEmpty()) {
            return Direction.UP;
        }

        // Step 4: was IDLE → choose direction toward first pending work
        if (!upStops.isEmpty() && downStops.isEmpty()) {
            return Direction.UP;
        }
        if (!downStops.isEmpty() && upStops.isEmpty()) {
            return Direction.DOWN;
        }

        // Both sides have stops while IDLE: go toward nearer side
        int floor = elevator.getCurrentFloor();
        int up = upStops.first();
        int down = downStops.last();
        return Math.abs(up - floor) <= Math.abs(down - floor) ? Direction.UP : Direction.DOWN;
    }
}
