package com.airline.airline.dto;

import com.airline.airline.dto.enums.ResponseType;
import com.airline.airline.models.FlightSeat;

import java.util.ArrayList;
import java.util.List;

public class SeatMapResponseDTO {
    private ResponseType responseType;
    private String message;
    private List<FlightSeat> seats = new ArrayList<>();

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FlightSeat> getSeats() {
        return seats;
    }

    public void setSeats(List<FlightSeat> seats) {
        this.seats = seats;
    }
}
