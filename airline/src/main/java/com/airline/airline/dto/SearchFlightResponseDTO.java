package com.airline.airline.dto;

import com.airline.airline.dto.enums.ResponseType;
import com.airline.airline.models.Flight;

import java.util.ArrayList;
import java.util.List;

public class SearchFlightResponseDTO {
    private ResponseType responseType;
    private String message;
    private List<Flight> flights = new ArrayList<>();

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

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
