package com.elevatorsystem.elevator.model;

/**
 * Inside-cabin request.
 * Example: passenger in elevator #2 presses floor 9.
 */
public class CabinRequest {

    private final int elevatorId;
    private final int destinationFloor;

    public CabinRequest(int elevatorId, int destinationFloor) {
        this.elevatorId = elevatorId;
        this.destinationFloor = destinationFloor;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    @Override
    public String toString() {
        return "CabinRequest{elevatorId=" + elevatorId + ", destinationFloor=" + destinationFloor + "}";
    }
}
