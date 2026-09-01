package com.palash.parkinglot.strategies.slotAllocationStrategy;

import com.palash.parkinglot.enums.VehicleType;
import com.palash.parkinglot.models.Gate;
import com.palash.parkinglot.models.ParkingSlot;

public interface SlotAllocationStrategy {
    public ParkingSlot getParkingSlot(Gate gate, VehicleType vehicleType);
}
