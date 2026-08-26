package com.elevatorsystem.elevator.strategy;

import com.elevatorsystem.elevator.enums.Direction;
import com.elevatorsystem.elevator.model.Elevator;

/**
 * Decides NEXT stop / direction for ONE elevator (scheduling).
 * LOOK is our concrete implementation for the 1-hour design.
 */
public interface SchedulingStrategy {

    /**
     * @return next floor to aim for, or null if nothing pending
     */
    Integer nextStop(Elevator elevator);

    /**
     * LOOK core: keep going same way if work remains; else reverse; else IDLE.
     */
    Direction resolveDirection(Elevator elevator);
}
