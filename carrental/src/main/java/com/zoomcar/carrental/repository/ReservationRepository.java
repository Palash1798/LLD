package com.zoomcar.carrental.repository;

import com.zoomcar.carrental.models.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class ReservationRepository {
    private final Map<Long, Reservation> reservationTable = new TreeMap<>();
    private long previousId = 0L;

    public Reservation saveReservation(Reservation reservation) {
        previousId += 1;
        reservation.setId(previousId);
        reservationTable.put(previousId, reservation);
        return reservation;
    }

    public Optional<Reservation> findReservationById(long reservationId) {
        return Optional.ofNullable(reservationTable.get(reservationId));
    }

    public List<Reservation> findReservationsByCarId(long carId) {
        List<Reservation> reservations = new ArrayList<>();
        for (Reservation reservation : reservationTable.values()) {
            if (reservation.getCarId() == carId) {
                reservations.add(reservation);
            }
        }
        return reservations;
    }
}
