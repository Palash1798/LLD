package com.airline.airline.controller;

import com.airline.airline.dto.BookFlightRequestDTO;
import com.airline.airline.dto.BookFlightResponseDTO;
import com.airline.airline.dto.CheckInResponseDTO;
import com.airline.airline.dto.SearchFlightRequestDTO;
import com.airline.airline.dto.SearchFlightResponseDTO;
import com.airline.airline.dto.SeatMapResponseDTO;
import com.airline.airline.dto.enums.ResponseType;
import com.airline.airline.models.Booking;
import com.airline.airline.models.BoardingPass;
import com.airline.airline.services.BookingService;
import com.airline.airline.services.CheckInService;
import com.airline.airline.services.FlightSearchService;

import java.util.List;

public class BookingController {

    private final FlightSearchService flightSearchService;
    private final BookingService bookingService;
    private final CheckInService checkInService;

    public BookingController(FlightSearchService flightSearchService,
                             BookingService bookingService,
                             CheckInService checkInService) {
        this.flightSearchService = flightSearchService;
        this.bookingService = bookingService;
        this.checkInService = checkInService;
    }

    public SearchFlightResponseDTO searchFlights(SearchFlightRequestDTO request) {
        SearchFlightResponseDTO response = new SearchFlightResponseDTO();
        try {
            response.setFlights(flightSearchService.search(
                    request.getSourceCode(), request.getDestCode(), request.getDate()));
            response.setResponseType(ResponseType.SUCCESS);
        } catch (Exception e) {
            response.setResponseType(ResponseType.FAILURE);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public SeatMapResponseDTO getSeatMap(long flightId) {
        SeatMapResponseDTO response = new SeatMapResponseDTO();
        try {
            response.setSeats(bookingService.getSeatMap(flightId));
            response.setResponseType(ResponseType.SUCCESS);
        } catch (Exception e) {
            response.setResponseType(ResponseType.FAILURE);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    /**
     * Book Flight entry point.
     * Controller only delegates to BookingService — no business logic here.
     */
    public BookFlightResponseDTO bookFlight(BookFlightRequestDTO request) {
        BookFlightResponseDTO response = new BookFlightResponseDTO();
        try {
            // Delegate entire book flow to BookingService.createBooking() (Steps 1–10)
            Booking booking = bookingService.createBooking(
                    request.getPassengerId(),
                    request.getFlightId(),
                    request.getSeatIds(),
                    request.getPaymentMethod(),
                    request.getIdempotencyKey());
            response.setBooking(booking);
            response.setResponseType(ResponseType.SUCCESS);
        } catch (Exception e) {
            response.setResponseType(ResponseType.FAILURE);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public BookFlightResponseDTO cancelBooking(String pnr) {
        BookFlightResponseDTO response = new BookFlightResponseDTO();
        try {
            Booking booking = bookingService.cancelBooking(pnr);
            response.setBooking(booking);
            response.setResponseType(ResponseType.SUCCESS);
        } catch (Exception e) {
            response.setResponseType(ResponseType.FAILURE);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public CheckInResponseDTO checkIn(String pnr) {
        CheckInResponseDTO response = new CheckInResponseDTO();
        try {
            BoardingPass boardingPass = checkInService.checkIn(pnr);
            response.setBoardingPass(boardingPass);
            response.setResponseType(ResponseType.SUCCESS);
        } catch (Exception e) {
            response.setResponseType(ResponseType.FAILURE);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public List<Booking> getBookingHistory(long passengerId) {
        return bookingService.getBookingHistory(passengerId);
    }
}
