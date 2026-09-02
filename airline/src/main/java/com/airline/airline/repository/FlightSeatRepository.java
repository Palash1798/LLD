package com.airline.airline.repository;

import com.airline.airline.models.FlightSeat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class FlightSeatRepository {
    private final Map<Long, FlightSeat> seatTable = new TreeMap<>();
    private long previousId = 0L;

    public FlightSeat save(FlightSeat seat) {
        if (seat.getId() == 0) {
            previousId += 1;
            seat.setId(previousId);
        }
        seatTable.put(seat.getId(), seat);
        return seat;
    }

    public List<FlightSeat> saveAll(List<FlightSeat> seats) {
        List<FlightSeat> saved = new ArrayList<>();
        for (FlightSeat seat : seats) {
            saved.add(save(seat));
        }
        return saved;
    }

    public Optional<FlightSeat> findById(long id) {
        return Optional.ofNullable(seatTable.get(id));
    }

    public List<FlightSeat> findAllById(List<Long> ids) {
        List<FlightSeat> seats = new ArrayList<>();
        for (Long id : ids) {
            FlightSeat seat = seatTable.get(id);
            if (seat != null) {
                seats.add(seat);
            }
        }
        return seats;
    }

    public List<FlightSeat> findByFlightId(long flightId) {
        List<FlightSeat> seats = new ArrayList<>();
        for (FlightSeat seat : seatTable.values()) {
            if (seat.getFlightId() == flightId) {
                seats.add(seat);
            }
        }
        return seats;
    }
}
