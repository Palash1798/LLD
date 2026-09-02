package com.airline.airline.repository;

import com.airline.airline.models.Passenger;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class PassengerRepository {
    private final Map<Long, Passenger> passengerTable = new TreeMap<>();
    private long previousId = 0L;

    public Passenger save(Passenger passenger) {
        if (passenger.getId() == 0) {
            previousId += 1;
            passenger.setId(previousId);
        }
        passengerTable.put(passenger.getId(), passenger);
        return passenger;
    }

    public Optional<Passenger> findById(long id) {
        return Optional.ofNullable(passengerTable.get(id));
    }
}
