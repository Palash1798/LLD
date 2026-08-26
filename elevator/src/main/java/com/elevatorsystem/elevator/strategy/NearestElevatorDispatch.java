package com.elevatorsystem.elevator.strategy;

import com.elevatorsystem.elevator.enums.Direction;
import com.elevatorsystem.elevator.model.Elevator;
import com.elevatorsystem.elevator.model.HallRequest;

import java.util.List;

/**
 * Feature #1 helper: assign hall call to the nearest elevator.
 *
 * Steps:
 * 1) Score each elevator by |currentFloor - request.floor|
 * 2) Prefer elevators already moving toward the request floor (tie-break)
 * 3) Return the best elevator
 */
public class NearestElevatorDispatch implements DispatchStrategy {

    @Override
    public Elevator chooseElevator(HallRequest request, List<Elevator> elevators) {
        Elevator best = null;
        int bestScore = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            int distance = Math.abs(elevator.getCurrentFloor() - request.getFloor());
            int score = distance;

            // Tie-break bonus: already heading toward the hall floor → slightly better
            if (isMovingToward(elevator, request.getFloor())) {
                score -= 1;
            }

            if (score < bestScore) {
                bestScore = score;
                best = elevator;
            }
        }
        return best;
    }

    private boolean isMovingToward(Elevator elevator, int targetFloor) {
        Direction direction = elevator.getDirection();
        int floor = elevator.getCurrentFloor();
        if (direction == Direction.UP && targetFloor > floor) {
            return true;
        }
        if (direction == Direction.DOWN && targetFloor < floor) {
            return true;
        }
        return false;
    }
}
