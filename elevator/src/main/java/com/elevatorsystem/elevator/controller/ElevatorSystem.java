package com.elevatorsystem.elevator.controller;

import com.elevatorsystem.elevator.model.CabinRequest;
import com.elevatorsystem.elevator.enums.Direction;
import com.elevatorsystem.elevator.model.Elevator;
import com.elevatorsystem.elevator.model.HallRequest;
import com.elevatorsystem.elevator.strategy.LookStrategy;
import com.elevatorsystem.elevator.strategy.NearestElevatorDispatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Facade — the only class a demo / interviewer "client" should call.
 *
 * Creates M elevators, wires LOOK + nearest dispatch, exposes 4 APIs:
 * requestElevator, selectFloor, tick, getElevators
 *
 * Note: no separate ElevatorStatus DTO — Elevator itself is the single source of truth.
 */
public class ElevatorSystem {

    private final ElevatorController controller;

    /**
     * @param totalFloors   N floors in building (1..N)
     * @param elevatorCount M elevators
     */
    public ElevatorSystem(int totalFloors, int elevatorCount) {
        LookStrategy look = new LookStrategy();
        List<Elevator> elevators = new ArrayList<>();

        // Spread elevators: e.g. 10 floors + 2 cars → E1@1, E2@6 (matches README scenario)
        for (int i = 1; i <= elevatorCount; i++) {
            int startFloor = 1 + (i - 1) * (totalFloors / elevatorCount);
            startFloor = Math.min(startFloor, totalFloors);
            elevators.add(new Elevator(i, startFloor, look));
        }

        this.controller = new ElevatorController(
                elevators,
                new NearestElevatorDispatch(),
                totalFloors
        );
    }

    /** Feature #1 — hall / outside call */
    public void requestElevator(int floor, Direction direction) {
        controller.handleHallRequest(new HallRequest(floor, direction));
    }

    /** Feature #1 — cabin / inside call */
    public void selectFloor(int elevatorId, int floor) {
        controller.handleCabinRequest(new CabinRequest(elevatorId, floor));
    }

    /** Feature #3 — simulate one time unit */
    public void tick() {
        controller.tick();
    }

    public void tick(int times) {
        for (int i = 0; i < times; i++) {
            tick();
        }
    }

    public List<Elevator> getElevators() {
        return controller.getElevators();
    }

    public void printStatus(String label) {
        System.out.println("--- " + label + " ---");
        for (Elevator elevator : getElevators()) {
            System.out.println("  " + elevator);
        }
    }
}
