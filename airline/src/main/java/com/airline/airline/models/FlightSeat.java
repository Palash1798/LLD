package com.airline.airline.models;

import com.airline.airline.enums.FlightSeatStatus;
import com.airline.airline.enums.SeatClass;

import java.time.LocalDateTime;

public class FlightSeat extends BaseModel {
    private long flightId;
    private String seatNumber;
    private SeatClass seatClass;
    private FlightSeatStatus status;
    private LocalDateTime lockedAt;
    private Long lockedByPassengerId;

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(long flightId) {
        this.flightId = flightId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass;
    }

    public FlightSeatStatus getStatus() {
        return status;
    }

    public void setStatus(FlightSeatStatus status) {
        this.status = status;
    }

    public LocalDateTime getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(LocalDateTime lockedAt) {
        this.lockedAt = lockedAt;
    }

    public Long getLockedByPassengerId() {
        return lockedByPassengerId;
    }

    public void setLockedByPassengerId(Long lockedByPassengerId) {
        this.lockedByPassengerId = lockedByPassengerId;
    }
}
