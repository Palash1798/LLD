package com.zoomcar.carrental.dto;

import com.zoomcar.carrental.dto.enums.ResponseType;
import com.zoomcar.carrental.models.Reservation;

public class BookCarResponseDTO {
    private Reservation reservation;
    private String message;
    private ResponseType responseType;

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }
}
