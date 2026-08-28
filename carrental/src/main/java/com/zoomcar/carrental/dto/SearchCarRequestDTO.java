package com.zoomcar.carrental.dto;

import com.zoomcar.carrental.models.Car;

import java.time.LocalDate;
import java.util.List;

public class SearchCarRequestDTO {
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
