package com.airline.airline.services;

import com.airline.airline.models.Flight;
import com.airline.airline.repository.AirportRepository;
import com.airline.airline.repository.FlightRepository;

import java.time.LocalDate;
import java.util.List;

public class FlightSearchService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;

    public FlightSearchService(FlightRepository flightRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }

    public List<Flight> search(String sourceCode, String destCode, LocalDate date) {
        long sourceId = airportRepository.findByCode(sourceCode)
                .orElseThrow(() -> new IllegalArgumentException("Source airport not found: " + sourceCode))
                .getId();
        long destId = airportRepository.findByCode(destCode)
                .orElseThrow(() -> new IllegalArgumentException("Destination airport not found: " + destCode))
                .getId();
        return flightRepository.findByRouteAndDate(sourceId, destId, date);
    }
}
