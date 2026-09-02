package com.airline.airline.dto;

import com.airline.airline.dto.enums.ResponseType;
import com.airline.airline.models.BoardingPass;

public class CheckInResponseDTO {
    private ResponseType responseType;
    private String message;
    private BoardingPass boardingPass;

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

    public BoardingPass getBoardingPass() {
        return boardingPass;
    }

    public void setBoardingPass(BoardingPass boardingPass) {
        this.boardingPass = boardingPass;
    }
}
