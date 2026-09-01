package com.palash.parkinglot.strategies.slotAllocationStrategy;

import com.palash.parkinglot.enums.VehicleType;
import com.palash.parkinglot.models.Gate;
import com.palash.parkinglot.models.ParkingSlot;

public class NearestSlotAllocationStrategy implements SlotAllocationStrategy{
    @Override
    public ParkingSlot getParkingSlot(Gate gate, VehicleType vehicleType) {
        return null;
    }
}
