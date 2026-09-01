package com.palash.parkinglot.models;

import com.palash.parkinglot.enums.FeeCalculationStrategyType;
import com.palash.parkinglot.enums.ParkingLotStatus;
import com.palash.parkinglot.enums.SlotAllocationStrategyType;
import com.palash.parkinglot.enums.VehicleType;
import com.palash.parkinglot.strategies.feeCalculationStrategy.FeeCalculationStrategy;
import com.palash.parkinglot.strategies.slotAllocationStrategy.SlotAllocationStrategy;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot extends BaseModel{
    private List<ParkingFloor> parkingFloors;
    private List<Gate> gates;
    private List<VehicleType> allowedVehicleTypes;
    private ParkingLotStatus parkingLotStatus;
    private FeeCalculationStrategyType feeCalculationStrategyType;
    private SlotAllocationStrategyType slotAllocationStrategyType;

    public ParkingLot() {
        this.parkingFloors = new ArrayList<>();
        this.gates = new ArrayList<>();
        this.allowedVehicleTypes = new ArrayList<>();
        this.parkingLotStatus = ParkingLotStatus.OPEN;
        this.feeCalculationStrategyType = FeeCalculationStrategyType.HOURLY;
        this.slotAllocationStrategyType = SlotAllocationStrategyType.RANDOM;
    }

    public List<ParkingFloor> getParkingFloors() {
        return parkingFloors;
    }

    public void setParkingFloors(List<ParkingFloor> parkingFloors) {
        this.parkingFloors = parkingFloors;
    }

    public List<Gate> getGates() {
        return gates;
    }

    public void setGates(List<Gate> gates) {
        this.gates = gates;
    }

    public List<VehicleType> getAllowedVehicleTypes() {
        return allowedVehicleTypes;
    }

    public void setAllowedVehicleTypes(List<VehicleType> allowedVehicleTypes) {
        this.allowedVehicleTypes = allowedVehicleTypes;
    }

    public ParkingLotStatus getParkingLotStatus() {
        return parkingLotStatus;
    }

    public void setParkingLotStatus(ParkingLotStatus parkingLotStatus) {
        this.parkingLotStatus = parkingLotStatus;
    }

    public FeeCalculationStrategyType getFeeCalculationStrategyType() {
        return feeCalculationStrategyType;
    }

    public void setFeeCalculationStrategyType(FeeCalculationStrategyType feeCalculationStrategyType) {
        this.feeCalculationStrategyType = feeCalculationStrategyType;
    }

    public SlotAllocationStrategyType getSlotAllocationStrategyType() {
        return slotAllocationStrategyType;
    }

    public void setSlotAllocationStrategyType(SlotAllocationStrategyType slotAllocationStrategyType) {
        this.slotAllocationStrategyType = slotAllocationStrategyType;
    }
}
