package com.palash.parkinglot.dto;

import com.palash.parkinglot.dto.enums.ResponseType;
import com.palash.parkinglot.models.Ticket;

public class IssueTicketResponseDTO {
//    private String ticketNumber;
//    private int parkingSlotNUmber;
//               we can either give enitre ticket object or we can specify special attritbutes
    private Ticket ticket;
    private String message;
    private ResponseType responseType;

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
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
