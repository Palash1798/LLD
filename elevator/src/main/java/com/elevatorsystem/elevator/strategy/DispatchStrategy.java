package com.elevatorsystem.elevator.strategy;

import com.elevatorsystem.elevator.model.Elevator;
import com.elevatorsystem.elevator.model.HallRequest;

import java.util.List;

/**
 * Chooses WHICH elevator should take a hall call (dispatch).
 * Different from SchedulingStrategy (which orders stops inside one elevator).
 */
public interface DispatchStrategy {

    Elevator chooseElevator(HallRequest request, List<Elevator> elevators);
}
