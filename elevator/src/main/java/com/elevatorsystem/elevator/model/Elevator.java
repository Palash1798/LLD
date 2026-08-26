package com.elevatorsystem.elevator.model;

import com.elevatorsystem.elevator.enums.Direction;
import com.elevatorsystem.elevator.enums.DoorState;
import com.elevatorsystem.elevator.strategy.SchedulingStrategy;

import java.util.Set;
import java.util.TreeSet;

/**
 * One physical elevator car.
 *
 * Pending floors (what ElevatorStatus used to show):
 *   We do NOT keep a third "pendingFloors" field (that would duplicate data).
 *   Instead we keep TWO TreeSets for LOOK:
 *     upStops   = floors to serve while going UP
 *     downStops = floors to serve while going DOWN
 *   getPendingFloors() = upStops ∪ downStops  (for printing / queries only)
 *
 * Why TreeSet (not HashSet / List)?
 *   - Set  → no duplicate floors
 *   - TreeSet → floors stay sorted, so LOOK can take first() / last() easily
 */
public class Elevator {

    private final int id;
    private int currentFloor;
    private Direction direction;
    private DoorState doorState;

    /** Pending stops in the UP direction (sorted low → high). */
    private final TreeSet<Integer> upStops = new TreeSet<>();

    /** Pending stops in the DOWN direction (sorted low → high; LOOK uses last()). */
    private final TreeSet<Integer> downStops = new TreeSet<>();

    private final SchedulingStrategy schedulingStrategy;

    public Elevator(int id, int startFloor, SchedulingStrategy schedulingStrategy) {
        this.id = id;
        this.currentFloor = startFloor;
        this.direction = Direction.IDLE;
        this.doorState = DoorState.CLOSED;
        this.schedulingStrategy = schedulingStrategy;
    }

    /**
     * Feature #1: accept a hall call already assigned to THIS elevator.
     *
     * Steps:
     * 1) Classify pickup floor by WHERE the car must travel (vs currentFloor)
     *    — this keeps LOOK moving toward the passenger (1-hour simplification)
     * 2) If we were IDLE, set direction toward that floor / ask LOOK
     *
     * Note: Hall button direction (UP/DOWN) is used mainly for realism in the request
     * object; full "only pick up same-direction passengers" is out of 1-hour scope.
     */
    public void addHallRequest(HallRequest request) {
        int floor = request.getFloor();

        // Same floor as us → just open door (passenger boards here)
        if (floor == currentFloor) {
            doorState = DoorState.OPEN;
            // Face the direction the passenger wants to travel next
            if (request.getDirection() == Direction.UP || request.getDirection() == Direction.DOWN) {
                direction = request.getDirection();
            }
            return;
        }

        // Step 1: put pickup floor on the side we must travel to reach it
        if (floor > currentFloor) {
            upStops.add(floor);
        } else {
            downStops.add(floor);
        }

        // Step 2: leave IDLE if needed
        if (direction == Direction.IDLE) {
            direction = schedulingStrategy.resolveDirection(this);
        }
    }

    /**
     * Feature #1: passenger inside selects a destination.
     *
     * Steps:
     * 1) Compare destination with currentFloor
     * 2) Add to upStops or downStops
     * 3) If IDLE, set direction toward that floor
     */
    public void addCabinRequest(int floor) {
        if (floor == currentFloor) {
            doorState = DoorState.OPEN;
            return;
        }

        if (floor > currentFloor) {
            upStops.add(floor);
        } else {
            downStops.add(floor);
        }

        if (direction == Direction.IDLE) {
            direction = floor > currentFloor ? Direction.UP : Direction.DOWN;
        }
    }

    /**
     * Feature #3: one simulation step.
     *
     * Steps (important — memorize this order):
     * 1) If door is OPEN → close it and finish this tick (boarding took the tick)
     * 2) If current floor is a pending stop → open door, remove stop, maybe update direction
     * 3) If no pending requests → become IDLE
     * 4) Ask LOOK to resolve direction (may reverse here)
     * 5) Move one floor
     * 6) If we landed on a stop → open door and clear it
     *
     * So: 1 tick ≈ 1 small unit of time (move one floor, or open/close door).
     */
    public void tick() {
        // Step 1: door open consumes this tick
        if (doorState == DoorState.OPEN) {
            doorState = DoorState.CLOSED;
            if (!hasPendingRequests()) {
                direction = Direction.IDLE;
            }
            return;
        }

        // Step 2: already standing on a requested floor?
        if (serveIfNeeded()) {
            return;
        }

        // Step 3: nothing to do
        if (!hasPendingRequests()) {
            direction = Direction.IDLE;
            return;
        }

        // Step 4: LOOK may keep / reverse direction
        direction = schedulingStrategy.resolveDirection(this);
        if (direction == Direction.IDLE) {
            return;
        }

        // Step 5: move exactly one floor
        moveOneFloor();

        // Step 6: serve if we arrived
        serveIfNeeded();
    }

    /**
     * @return true if we opened the door (caller should end the tick)
     */
    private boolean serveIfNeeded() {
        // Clear this floor from both sets (pickup or cabin destination)
        boolean served = false;
        if (upStops.remove(currentFloor)) {
            served = true;
        }
        if (downStops.remove(currentFloor)) {
            served = true;
        }

        if (served) {
            doorState = DoorState.OPEN;
            // LOOK updates direction after stop is cleared (may reverse / idle)
            direction = schedulingStrategy.resolveDirection(this);
        }
        return served;
    }

    private void moveOneFloor() {
        if (direction == Direction.UP) {
            currentFloor++;
        } else if (direction == Direction.DOWN) {
            currentFloor--;
        }
    }

    public boolean hasPendingRequests() {
        return !upStops.isEmpty() || !downStops.isEmpty();
    }

    /**
     * All pending floors = upStops + downStops.
     * This replaces the old ElevatorStatus.pendingFloors field (computed, not stored twice).
     */
    public Set<Integer> getPendingFloors() {
        TreeSet<Integer> pending = new TreeSet<>();
        pending.addAll(upStops);
        pending.addAll(downStops);
        return pending;
    }

    public int getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public DoorState getDoorState() {
        return doorState;
    }

    public TreeSet<Integer> getUpStops() {
        return upStops;
    }

    public TreeSet<Integer> getDownStops() {
        return downStops;
    }

    @Override
    public String toString() {
        return "E" + id
                + " floor=" + currentFloor
                + " dir=" + direction
                + " door=" + doorState
                + " pending=" + getPendingFloors();
    }
}
