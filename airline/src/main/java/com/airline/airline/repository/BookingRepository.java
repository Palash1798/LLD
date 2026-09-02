package com.airline.airline.repository;

import com.airline.airline.models.Booking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class BookingRepository {
    private final Map<Long, Booking> bookingTable = new TreeMap<>();
    private final Map<String, Booking> pnrIndex = new TreeMap<>();
    private long previousId = 0L;

    public Booking save(Booking booking) {
        if (booking.getId() == 0) {
            previousId += 1;
            booking.setId(previousId);
        }
        bookingTable.put(booking.getId(), booking);
        if (booking.getPnr() != null) {
            pnrIndex.put(booking.getPnr(), booking);
        }
        return booking;
    }

    public Optional<Booking> findById(long id) {
        return Optional.ofNullable(bookingTable.get(id));
    }

    public Optional<Booking> findByPnr(String pnr) {
        return Optional.ofNullable(pnrIndex.get(pnr));
    }

    public List<Booking> findByPassengerId(long passengerId) {
        List<Booking> bookings = new ArrayList<>();
        for (Booking booking : bookingTable.values()) {
            if (booking.getPassengerId() == passengerId) {
                bookings.add(booking);
            }
        }
        return bookings;
    }
}
