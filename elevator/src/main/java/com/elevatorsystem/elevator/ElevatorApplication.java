package com.elevatorsystem.elevator;

import com.elevatorsystem.elevator.controller.ElevatorSystem;
import com.elevatorsystem.elevator.enums.Direction;
import com.elevatorsystem.elevator.model.Elevator;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElevatorApplication {

//	public static void main(String[] args) {
//		SpringApplication.run(ElevatorApplication.class, args);
//	}

    /**
     * Runnable revision demo — covers only 3 features (1-hour scope):
     *
     * Feature 1: Hall call + Cabin call (+ nearest elevator dispatch)
     * Feature 2: LOOK scheduling (serve along direction, reverse when side empties)
     * Feature 3: Discrete tick() simulation
     *
     * Run ElevatorApplication.main from the IDE (Spring Boot not required for this demo).
     */
    public static void main(String[] args) {
        // --------------------------------------------------
        // SETUP: 10 floors, 2 elevators → E1@1, E2@6
        // --------------------------------------------------
        ElevatorSystem system = new ElevatorSystem(10, 2);
        system.printStatus("Initial");

        // --------------------------------------------------
        // FEATURE 1a — HALL CALL
        // Floor 4 UP → NearestElevatorDispatch picks E2 (|6-4|=2 < |1-4|=3)
        // --------------------------------------------------
        System.out.println("\n>> Hall call: floor 4 UP");
        system.requestElevator(4, Direction.UP);
        system.printStatus("After hall call assigned");

        // --------------------------------------------------
        // FEATURE 3 — TICK until car reaches floor 4 and opens door
        // --------------------------------------------------
        System.out.println("\n>> Ticking until an elevator opens at floor 4...");
        int servingElevatorId = 2;
        for (int i = 1; i <= 10; i++) {
            system.tick();
            system.printStatus("Tick " + i);
            Elevator openAt4 = findOpenAt(system, 4);
            if (openAt4 != null) {
                servingElevatorId = openAt4.getId();
                break;
            }
        }

        // --------------------------------------------------
        // FEATURE 1b — CABIN CALL (passenger boards, presses 9)
        // --------------------------------------------------
        System.out.println("\n>> Cabin call: E" + servingElevatorId + " -> floor 9");
        system.selectFloor(servingElevatorId, 9);

        // --------------------------------------------------
        // FEATURE 2 — LOOK keeps UP until 9, then IDLE
        // (does NOT travel to floor 10 — difference vs SCAN)
        // --------------------------------------------------
        System.out.println("\n>> LOOK: travel UP to 9 only...");
        for (int i = 1; i <= 15; i++) {
            system.tick();
            system.printStatus("LOOK tick " + i);
            if (isIdleAt(system, servingElevatorId, 9)) {
                break;
            }
        }

        // --------------------------------------------------
        // FEATURE 2 (bonus) — LOOK reverse
        // From floor 5: cabin 8 (UP side) + cabin 2 (DOWN side)
        // Expect: finish UP to 8, then reverse DOWN to 2
        // --------------------------------------------------
        System.out.println("\n>> LOOK reverse demo on E1");
        system.requestElevator(5, Direction.UP);
        for (int i = 0; i < 8; i++) {
            system.tick();
        }
        system.printStatus("E1 should be near 5");

        system.selectFloor(1, 8);
        system.selectFloor(1, 2);
        system.printStatus("E1 pending = {8, 2}");

        System.out.println("\n>> Ticking: expect UP toward 8, then DOWN toward 2...");
        for (int i = 1; i <= 25; i++) {
            system.tick();
            system.printStatus("Reverse tick " + i);
            Elevator e1 = system.getElevators().get(0);
            if (e1.getPendingFloors().isEmpty()
                    && e1.getDirection() == Direction.IDLE
                    && e1.getDoorState().name().equals("CLOSED")) {
                break;
            }
        }

        System.out.println("\nDone. Study: Elevator.tick(), LookStrategy, NearestElevatorDispatch.");
    }

    private static Elevator findOpenAt(ElevatorSystem system, int floor) {
        for (Elevator elevator : system.getElevators()) {
            if (elevator.getCurrentFloor() == floor && elevator.getDoorState().name().equals("OPEN")) {
                return elevator;
            }
        }
        return null;
    }

    private static boolean isIdleAt(ElevatorSystem system, int elevatorId, int floor) {
        for (Elevator elevator : system.getElevators()) {
            if (elevator.getId() == elevatorId
                    && elevator.getCurrentFloor() == floor
                    && elevator.getPendingFloors().isEmpty()
                    && elevator.getDirection() == Direction.IDLE
                    && elevator.getDoorState().name().equals("CLOSED")) {
                return true;
            }
        }
        return false;
    }
}
