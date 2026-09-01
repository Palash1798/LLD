package com.palash.parkinglot.repository;

import com.palash.parkinglot.models.Gate;
import com.palash.parkinglot.models.Vehicle;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class VehicleRepository {
    private Map<String, Vehicle> vehicleTable = new TreeMap<>();
    public Optional<Vehicle> getVehicleByVehicleNumber(String vehicleNumber){
        // String response = db.execute("select * from vehicle where vehicle_number = vehicleNumber");
        // vehicle vehicleObject = convertResponseToVehicle(response);
        // if(vehicleObject != null){
        //       return Optional.of(vehicleObject);
        // }
        if(vehicleTable.containsKey(vehicleNumber)){
            return Optional.of(vehicleTable.get(vehicleNumber));
        }
        return Optional.empty();
    }

    public Vehicle saveVehicle(Vehicle vehicle){
        // db.execute("Insert into vehicle (vehicle_number, owner_name, vehicle_type) VALUES (vehicle.getNumber()..)");
        // String response = db.execute("select * from vehicle where vehicle_number = vehicleNumber");
        // vehicle vehicleObject = convertResponseToVehicle(response);
        // if(vehicleObject != null){
        //       return Optional.of(vehicleObject);
        // }
        vehicleTable.put(vehicle.getVehicleNumber(), vehicle);
        return vehicle;
    }
}
