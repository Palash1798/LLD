package com.elevatorsystem.elevator.model;

import com.elevatorsystem.elevator.enums.Direction;

/**
 * Outside / floor-panel request.
 * Example: passenger on floor 4 presses UP.
 */
public class HallRequest {

    private final int floor;
    private final Direction direction; // UP or DOWN only (not IDLE)

    public HallRequest(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }

    public int getFloor() {
        return floor;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "HallRequest{floor=" + floor + ", direction=" + direction + "}";
    }
}
