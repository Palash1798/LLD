package com.airline.airline;

import com.airline.airline.controller.BookingController;
import com.airline.airline.dto.BookFlightRequestDTO;
import com.airline.airline.dto.BookFlightResponseDTO;
import com.airline.airline.dto.CheckInResponseDTO;
import com.airline.airline.dto.SearchFlightRequestDTO;
import com.airline.airline.dto.SearchFlightResponseDTO;
import com.airline.airline.dto.SeatMapResponseDTO;
import com.airline.airline.dto.enums.ResponseType;
import com.airline.airline.enums.PaymentMethod;
import com.airline.airline.models.Flight;
import com.airline.airline.models.FlightSeat;

import java.time.LocalDate;
import java.util.List;

public class Client {

    private final BookingController bookingController;

    public Client(BookingController bookingController) {
        this.bookingController = bookingController;
    }

    public void runTestCases() {
        System.out.println("========== Airline Management System - Demo ==========\n");
        testCase1_searchBookAndCheckIn();
        System.out.println();
        testCase2_doubleBookingShouldFail();
        System.out.println();
        testCase3_cancelBooking();
    }

    private void testCase1_searchBookAndCheckIn() {
        System.out.println("=== Test 1: Search, Book, Check-in ===");

        Flight flight = AirlineApplication.flightRepository.findById(1L).orElseThrow();
        LocalDate travelDate = flight.getDepartureTime().toLocalDate();

        SearchFlightRequestDTO searchRequest = new SearchFlightRequestDTO();
        searchRequest.setSourceCode("DEL");
        searchRequest.setDestCode("BOM");
        searchRequest.setDate(travelDate);

        SearchFlightResponseDTO searchResponse = bookingController.searchFlights(searchRequest);
        System.out.println("Search status: " + searchResponse.getResponseType());
        System.out.println("Flights found: " + searchResponse.getFlights().size());

        // Pre-book: view seat map (optional step before booking)
        SeatMapResponseDTO seatMap = bookingController.getSeatMap(flight.getId());
        System.out.println("Seat map status: " + seatMap.getResponseType());
        System.out.println("Total seats: " + seatMap.getSeats().size());

        List<Long> seatIds = findSeatIds(seatMap.getSeats(), List.of("12A", "12B"));

        // --- Book Flight flow starts here (BookingService Steps 1–10) ---
        BookFlightRequestDTO bookRequest = new BookFlightRequestDTO();
        bookRequest.setPassengerId(1L);           // Step 1 input: who is booking
        bookRequest.setFlightId(flight.getId()); // Step 2 input: which flight
        bookRequest.setSeatIds(seatIds);         // Steps 3–4 input: which seats
        bookRequest.setPaymentMethod(PaymentMethod.UPI);  // Step 8 input: how to pay
        bookRequest.setIdempotencyKey("req-book-001");      // Step 8a: safe retry key

        BookFlightResponseDTO bookResponse = bookingController.bookFlight(bookRequest);
        // On SUCCESS → PNR generated, seats BOOKED, booking CONFIRMED
        System.out.println("Book status: " + bookResponse.getResponseType());
        if (bookResponse.getResponseType() == ResponseType.SUCCESS) {
            System.out.println("PNR: " + bookResponse.getBooking().getPnr());
            System.out.println("Amount: " + bookResponse.getBooking().getTotalAmount());
            System.out.println("Booking status: " + bookResponse.getBooking().getStatus());

            CheckInResponseDTO checkInResponse = bookingController.checkIn(bookResponse.getBooking().getPnr());
            System.out.println("Check-in status: " + checkInResponse.getResponseType());
            if (checkInResponse.getResponseType() == ResponseType.SUCCESS) {
                System.out.println("Gate: " + checkInResponse.getBoardingPass().getGate());
                System.out.println("Seats: " + checkInResponse.getBoardingPass().getSeatNumbers());
            }
        } else {
            System.out.println("Message: " + bookResponse.getMessage());
        }
    }

    private void testCase2_doubleBookingShouldFail() {
        System.out.println("=== Test 2: Double booking same seat ===");

        long seatId = findSeatIdByNumber("14C");

        BookFlightRequestDTO bookRequest = new BookFlightRequestDTO();
        bookRequest.setPassengerId(1L);
        bookRequest.setFlightId(1L);
        bookRequest.setSeatIds(List.of(seatId));
        bookRequest.setPaymentMethod(PaymentMethod.UPI);
        bookRequest.setIdempotencyKey("req-book-002a");

        BookFlightResponseDTO firstBook = bookingController.bookFlight(bookRequest);
        System.out.println("First book status: " + firstBook.getResponseType());
        if (firstBook.getResponseType() == ResponseType.SUCCESS) {
            System.out.println("PNR: " + firstBook.getBooking().getPnr());
        }

        bookRequest.setPassengerId(2L);
        bookRequest.setIdempotencyKey("req-book-002b");
        BookFlightResponseDTO secondBook = bookingController.bookFlight(bookRequest);
        System.out.println("Second book status: " + secondBook.getResponseType());
        System.out.println("Message: " + secondBook.getMessage());
    }

    private void testCase3_cancelBooking() {
        System.out.println("=== Test 3: Cancel booking ===");

        long seatId = findSeatIdByNumber("1A");

        BookFlightRequestDTO bookRequest = new BookFlightRequestDTO();
        bookRequest.setPassengerId(2L);
        bookRequest.setFlightId(1L);
        bookRequest.setSeatIds(List.of(seatId));
        bookRequest.setPaymentMethod(PaymentMethod.WALLET);
        bookRequest.setIdempotencyKey("req-book-003");

        BookFlightResponseDTO bookResponse = bookingController.bookFlight(bookRequest);
        if (bookResponse.getResponseType() != ResponseType.SUCCESS) {
            System.out.println("Book failed: " + bookResponse.getMessage());
            return;
        }

        String pnr = bookResponse.getBooking().getPnr();
        System.out.println("Booked PNR: " + pnr);

        BookFlightResponseDTO cancelResponse = bookingController.cancelBooking(pnr);
        System.out.println("Cancel status: " + cancelResponse.getResponseType());
        if (cancelResponse.getResponseType() == ResponseType.SUCCESS) {
            System.out.println("Booking status after cancel: " + cancelResponse.getBooking().getStatus());
        } else {
            System.out.println("Message: " + cancelResponse.getMessage());
        }
    }

    private List<Long> findSeatIds(List<FlightSeat> seats, List<String> seatNumbers) {
        return seats.stream()
                .filter(s -> seatNumbers.contains(s.getSeatNumber()))
                .map(FlightSeat::getId)
                .toList();
    }

    private long findSeatIdByNumber(String seatNumber) {
        return AirlineApplication.flightSeatRepository.findByFlightId(1L).stream()
                .filter(s -> s.getSeatNumber().equals(seatNumber))
                .findFirst()
                .orElseThrow()
                .getId();
    }
}
