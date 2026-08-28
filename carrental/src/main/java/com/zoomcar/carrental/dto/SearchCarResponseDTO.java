package com.zoomcar.carrental.dto;

import com.zoomcar.carrental.dto.enums.ResponseType;
import com.zoomcar.carrental.models.Car;

import java.util.List;

public class SearchCarResponseDTO {
    private List<Car> cars;
    private String message;
    private ResponseType responseType;

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
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
