package com.airline.airline.dto;

import com.airline.airline.dto.enums.ResponseType;
import com.airline.airline.models.Booking;

public class BookFlightResponseDTO {
    private ResponseType responseType;
    private String message;
    private Booking booking;

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

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
