package com.airline.airline.services;

import com.airline.airline.enums.BookingStatus;
import com.airline.airline.exceptions.BookingNotFoundException;
import com.airline.airline.exceptions.CheckInNotAllowedException;
import com.airline.airline.exceptions.InvalidBookingStateException;
import com.airline.airline.models.BoardingPass;
import com.airline.airline.models.Booking;
import com.airline.airline.models.Flight;
import com.airline.airline.models.FlightSeat;
import com.airline.airline.repository.BoardingPassRepository;
import com.airline.airline.repository.BookingRepository;
import com.airline.airline.repository.FlightRepository;
import com.airline.airline.repository.FlightSeatRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CheckInService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final BoardingPassRepository boardingPassRepository;

    public CheckInService(BookingRepository bookingRepository,
                          FlightRepository flightRepository,
                          FlightSeatRepository flightSeatRepository,
                          BoardingPassRepository boardingPassRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.flightSeatRepository = flightSeatRepository;
        this.boardingPassRepository = boardingPassRepository;
    }

    public BoardingPass checkIn(String pnr)
            throws BookingNotFoundException, InvalidBookingStateException, CheckInNotAllowedException {
        Booking booking = bookingRepository.findByPnr(pnr)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found for PNR " + pnr));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new InvalidBookingStateException("Only confirmed bookings can check in");
        }

        Flight flight = flightRepository.findById(booking.getFlightId()).orElseThrow();
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(flight.getDepartureTime().minusHours(24))) {
            throw new CheckInNotAllowedException("Check-in opens 24 hours before departure");
        }
        if (now.isAfter(flight.getDepartureTime())) {
            throw new CheckInNotAllowedException("Flight has already departed");
        }

        List<FlightSeat> seats = flightSeatRepository.findAllById(booking.getFlightSeatIds());
        List<String> seatNumbers = new ArrayList<>();
        for (FlightSeat seat : seats) {
            seatNumbers.add(seat.getSeatNumber());
        }

        BoardingPass boardingPass = new BoardingPass();
        boardingPass.setBookingId(booking.getId());
        boardingPass.setSeatNumbers(seatNumbers);
        boardingPass.setGate("G12");
        boardingPass.setBoardingTime(flight.getDepartureTime().minusMinutes(45));
        boardingPass.setIssuedAt(now);
        boardingPassRepository.save(boardingPass);

        booking.setStatus(BookingStatus.CHECKED_IN);
        bookingRepository.save(booking);

        return boardingPass;
    }
}
