package com.palash.parkinglot.models;

import com.palash.parkinglot.enums.ParkingFloorStatus;
import com.palash.parkinglot.enums.VehicleType;

import java.util.ArrayList;
import java.util.List;

public class ParkingFloor extends BaseModel{
    private int floorNumber;
    private List<ParkingSlot> parkingSlots;
    private List<VehicleType> allowedVehicleTypes;
    private ParkingFloorStatus parkingFloorStatus;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.parkingSlots = new ArrayList<>();
        this.allowedVehicleTypes = new ArrayList<>();
        this.parkingFloorStatus = ParkingFloorStatus.OPEN;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public List<ParkingSlot> getParkingSlots() {
        return parkingSlots;
    }

    public void setParkingSlots(List<ParkingSlot> parkingSlots) {
        this.parkingSlots = parkingSlots;
    }

    public List<VehicleType> getAllowedVehicleTypes() {
        return allowedVehicleTypes;
    }

    public void setAllowedVehicleTypes(List<VehicleType> allowedVehicleTypes) {
        this.allowedVehicleTypes = allowedVehicleTypes;
    }

    public ParkingFloorStatus getParkingFloorStatus() {
        return parkingFloorStatus;
    }

    public void setParkingFloorStatus(ParkingFloorStatus parkingFloorStatus) {
        this.parkingFloorStatus = parkingFloorStatus;
    }
}
