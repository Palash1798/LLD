package com.zoomcar.carrental.models;

import com.zoomcar.carrental.enums.CarCategory;
import com.zoomcar.carrental.enums.CarStatus;

import java.math.BigDecimal;

public class Car extends BaseModel {
    private String model;
    private String registrationNumber;
    private CarCategory category;
    private BigDecimal dailyRate;
    private String location;
    private CarStatus status;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public CarCategory getCategory() {
        return category;
    }

    public void setCategory(CarCategory category) {
        this.category = category;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public CarStatus getStatus() {
        return status;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }
}
