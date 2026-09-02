package com.airline.airline.services;

import com.airline.airline.enums.FlightSeatStatus;
import com.airline.airline.models.FlightSeat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Handles seat soft-lock during booking (same idea as Book My Show ShowSeat BLOCKED).
 *
 * Seat lifecycle during book flow:
 *   AVAILABLE → BLOCKED (Step 5, while paying) → BOOKED (Step 9, payment success)
 *   AVAILABLE ← BLOCKED (Step 10, payment failed or lock expired after 10 min)
 */
public class SeatLockManager {

    private static final int LOCK_DURATION_MINUTES = 10;

    /** Used in Step 4 — seat is free if AVAILABLE, or BLOCKED but lock has expired. */
    public boolean isSeatAvailable(FlightSeat seat) {
        if (seat.getStatus() == FlightSeatStatus.AVAILABLE) {
            return true;
        }
        if (seat.getStatus() == FlightSeatStatus.BLOCKED && isLockExpired(seat)) {
            releaseSeat(seat);
            return true;
        }
        return false;
    }

    /** Step 5 — block seats for this passenger while payment runs. */
    public void lockSeats(List<FlightSeat> seats, long passengerId) {
        LocalDateTime now = LocalDateTime.now();
        for (FlightSeat seat : seats) {
            seat.setStatus(FlightSeatStatus.BLOCKED);
            seat.setLockedAt(now);
            seat.setLockedByPassengerId(passengerId);
        }
    }

    /** Step 9 — payment succeeded, permanently book the seats. */
    public void confirmSeats(List<FlightSeat> seats) {
        for (FlightSeat seat : seats) {
            seat.setStatus(FlightSeatStatus.BOOKED);
            seat.setLockedAt(null);
            seat.setLockedByPassengerId(null);
        }
    }

    /** Step 10 — payment failed; release seats so others can book. */
    public void releaseSeats(List<FlightSeat> seats) {
        for (FlightSeat seat : seats) {
            releaseSeat(seat);
        }
    }

    private void releaseSeat(FlightSeat seat) {
        seat.setStatus(FlightSeatStatus.AVAILABLE);
        seat.setLockedAt(null);
        seat.setLockedByPassengerId(null);
    }

    private boolean isLockExpired(FlightSeat seat) {
        if (seat.getLockedAt() == null) {
            return true;
        }
        return seat.getLockedAt().plusMinutes(LOCK_DURATION_MINUTES).isBefore(LocalDateTime.now());
    }
}
