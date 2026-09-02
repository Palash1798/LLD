package com.airline.airline.repository;

import com.airline.airline.models.Airport;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class AirportRepository {
    private final Map<Long, Airport> airportTable = new TreeMap<>();
    private long previousId = 0L;

    public Airport save(Airport airport) {
        if (airport.getId() == 0) {
            previousId += 1;
            airport.setId(previousId);
        }
        airportTable.put(airport.getId(), airport);
        return airport;
    }

    public Optional<Airport> findById(long id) {
        return Optional.ofNullable(airportTable.get(id));
    }

    public Optional<Airport> findByCode(String code) {
        return airportTable.values().stream()
                .filter(a -> a.getCode().equalsIgnoreCase(code))
                .findFirst();
    }
}
