package com.airline.airline.repository;

import com.airline.airline.models.Flight;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class FlightRepository {
    private final Map<Long, Flight> flightTable = new TreeMap<>();
    private long previousId = 0L;

    public Flight save(Flight flight) {
        if (flight.getId() == 0) {
            previousId += 1;
            flight.setId(previousId);
        }
        flightTable.put(flight.getId(), flight);
        return flight;
    }

    public Optional<Flight> findById(long id) {
        return Optional.ofNullable(flightTable.get(id));
    }

    public List<Flight> findByRouteAndDate(long sourceAirportId, long destAirportId, LocalDate date) {
        List<Flight> flights = new ArrayList<>();
        for (Flight flight : flightTable.values()) {
            if (flight.getSourceAirportId() == sourceAirportId
                    && flight.getDestAirportId() == destAirportId
                    && flight.getDepartureTime().toLocalDate().equals(date)) {
                flights.add(flight);
            }
        }
        return flights;
    }
}
