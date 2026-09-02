package com.airline.airline.models;

import com.airline.airline.enums.FlightStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Flight extends BaseModel {
    private String flightNumber;
    private long sourceAirportId;
    private long destAirportId;
    private long aircraftId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private BigDecimal baseFareEconomy;
    private FlightStatus status;

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public long getSourceAirportId() {
        return sourceAirportId;
    }

    public void setSourceAirportId(long sourceAirportId) {
        this.sourceAirportId = sourceAirportId;
    }

    public long getDestAirportId() {
        return destAirportId;
    }

    public void setDestAirportId(long destAirportId) {
        this.destAirportId = destAirportId;
    }

    public long getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(long aircraftId) {
        this.aircraftId = aircraftId;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public BigDecimal getBaseFareEconomy() {
        return baseFareEconomy;
    }

    public void setBaseFareEconomy(BigDecimal baseFareEconomy) {
        this.baseFareEconomy = baseFareEconomy;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }
}
