package com.elevatorsystem.elevator.controller;

import com.elevatorsystem.elevator.model.CabinRequest;
import com.elevatorsystem.elevator.model.Elevator;
import com.elevatorsystem.elevator.model.HallRequest;
import com.elevatorsystem.elevator.strategy.DispatchStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Brain of the building: assigns hall calls, forwards cabin calls, ticks all cars.
 *
 * Feature #1 flow (hall call):
 * 1) Receive HallRequest
 * 2) Ask DispatchStrategy for best elevator
 * 3) Add request to that elevator
 */
public class ElevatorController {

    private final List<Elevator> elevators;
    private final DispatchStrategy dispatchStrategy;
    private final int totalFloors;

    public ElevatorController(List<Elevator> elevators, DispatchStrategy dispatchStrategy, int totalFloors) {
        this.elevators = elevators;
        this.dispatchStrategy = dispatchStrategy;
        this.totalFloors = totalFloors;
    }

    public void handleHallRequest(HallRequest request) {
        // Step 1: basic validation (keep interview code short)
        if (!isValidFloor(request.getFloor())) {
            System.out.println("Ignored invalid hall floor: " + request.getFloor());
            return;
        }

        // Step 2: choose elevator (nearest)
        Elevator chosen = dispatchStrategy.chooseElevator(request, elevators);
        if (chosen == null) {
            System.out.println("No elevator available for " + request);
            return;
        }

        // Step 3: hand off to that elevator's stop sets
        System.out.println("Assigned " + request + " -> Elevator #" + chosen.getId());
        chosen.addHallRequest(request);
    }

    public void handleCabinRequest(CabinRequest request) {
        if (!isValidFloor(request.getDestinationFloor())) {
            System.out.println("Ignored invalid cabin floor: " + request.getDestinationFloor());
            return;
        }

        Elevator elevator = findById(request.getElevatorId());
        if (elevator == null) {
            System.out.println("Unknown elevator id: " + request.getElevatorId());
            return;
        }

        System.out.println("Cabin request on E" + request.getElevatorId()
                + " -> floor " + request.getDestinationFloor());
        elevator.addCabinRequest(request.getDestinationFloor());
    }

    /**
     * Feature #3: advance every elevator by one tick.
     */
    public void tick() {
        for (Elevator elevator : elevators) {
            elevator.tick();
        }
    }

    public List<Elevator> getElevators() {
        return new ArrayList<>(elevators);
    }

    private Elevator findById(int id) {
        for (Elevator elevator : elevators) {
            if (elevator.getId() == id) {
                return elevator;
            }
        }
        return null;
    }

    private boolean isValidFloor(int floor) {
        return floor >= 1 && floor <= totalFloors;
    }
}
