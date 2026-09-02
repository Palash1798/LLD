package com.airline.airline.services;

import com.airline.airline.enums.BookingStatus;
import com.airline.airline.enums.FlightStatus;
import com.airline.airline.enums.PaymentMethod;
import com.airline.airline.enums.PaymentStatus;
import com.airline.airline.exceptions.BookingNotFoundException;
import com.airline.airline.exceptions.FlightNotFoundException;
import com.airline.airline.exceptions.InvalidBookingStateException;
import com.airline.airline.exceptions.PassengerNotFoundException;
import com.airline.airline.exceptions.PaymentFailedException;
import com.airline.airline.exceptions.SeatNotAvailableException;
import com.airline.airline.factories.PaymentStrategyFactory;
import com.airline.airline.models.Booking;
import com.airline.airline.models.Flight;
import com.airline.airline.models.FlightSeat;
import com.airline.airline.models.Payment;
import com.airline.airline.paymentStrategies.PaymentStrategy;
import com.airline.airline.repository.BookingRepository;
import com.airline.airline.repository.FlightRepository;
import com.airline.airline.repository.FlightSeatRepository;
import com.airline.airline.repository.PassengerRepository;
import com.airline.airline.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingService {

    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final SeatLockManager seatLockManager;
    private final FareCalculatorService fareCalculatorService;

    public BookingService(FlightRepository flightRepository,
                          FlightSeatRepository flightSeatRepository,
                          PassengerRepository passengerRepository,
                          BookingRepository bookingRepository,
                          PaymentRepository paymentRepository,
                          SeatLockManager seatLockManager,
                          FareCalculatorService fareCalculatorService) {
        this.flightRepository = flightRepository;
        this.flightSeatRepository = flightSeatRepository;
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.seatLockManager = seatLockManager;
        this.fareCalculatorService = fareCalculatorService;
    }

    public List<FlightSeat> getSeatMap(long flightId) throws FlightNotFoundException {
        if (flightRepository.findById(flightId).isEmpty()) {
            throw new FlightNotFoundException("Flight not found for id " + flightId);
        }
        List<FlightSeat> seats = flightSeatRepository.findByFlightId(flightId);
        for (FlightSeat seat : seats) {
            seatLockManager.isSeatAvailable(seat);
        }
        return seats;
    }

    /**
     * Book Flight — core flow (memorize these steps for interview):
     *
     * Step 1  → Validate passenger exists
     * Step 2  → Validate flight exists and is not cancelled
     * Step 3  → Fetch seats and verify all seat IDs are valid
     * Step 4  → Check every seat belongs to this flight and is available
     * Step 5  → Soft-lock seats (BLOCKED for 10 min) — prevents double booking
     * Step 6  → Calculate total fare using FareCalculationStrategy per seat class
     * Step 7  → Create booking in PENDING state
     * Step 8  → Process payment via PaymentStrategy
     * Step 9  → On success: confirm seats (BOOKED), set booking CONFIRMED, generate PNR
     * Step 10 → On payment failure: release seats back to AVAILABLE and throw error
     */
    public Booking createBooking(long passengerId, long flightId, List<Long> seatIds,
                                 PaymentMethod paymentMethod, String idempotencyKey)
            throws PassengerNotFoundException, FlightNotFoundException, SeatNotAvailableException,
            PaymentFailedException {

        // Step 1: Validate passenger
        if (passengerRepository.findById(passengerId).isEmpty()) {
            throw new PassengerNotFoundException("Passenger not found for id " + passengerId);
        }

        // Step 2: Validate flight
        Optional<Flight> flightOptional = flightRepository.findById(flightId);
        if (flightOptional.isEmpty()) {
            throw new FlightNotFoundException("Flight not found for id " + flightId);
        }
        Flight flight = flightOptional.get();
        if (flight.getStatus() == FlightStatus.CANCELLED) {
            throw new FlightNotFoundException("Flight is cancelled");
        }

        // Step 3: Fetch seats by IDs
        List<FlightSeat> seats = flightSeatRepository.findAllById(seatIds);
        if (seats.size() != seatIds.size()) {
            throw new SeatNotAvailableException("One or more seats not found");
        }

        // Step 4: Validate seat availability (AVAILABLE or expired BLOCKED)
        for (FlightSeat seat : seats) {
            if (seat.getFlightId() != flightId) {
                throw new SeatNotAvailableException("All seats must belong to the same flight");
            }
            if (!seatLockManager.isSeatAvailable(seat)) {
                throw new SeatNotAvailableException("Seat " + seat.getSeatNumber() + " is not available");
            }
        }

        // Step 5: Soft-lock seats while payment is in progress
        seatLockManager.lockSeats(seats, passengerId);
        flightSeatRepository.saveAll(seats);

        // Step 6: Calculate fare (Economy × 1, Business × 2.5, First × 4)
        BigDecimal totalAmount = fareCalculatorService.calculateTotal(flight, seats);

        // Step 7: Create booking in PENDING state (not confirmed yet)
        Booking booking = new Booking();
        booking.setPassengerId(passengerId);
        booking.setFlightId(flightId);
        booking.setFlightSeatIds(new ArrayList<>(seatIds));
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalAmount(totalAmount);
        booking.setBookedAt(LocalDateTime.now());
        booking = bookingRepository.save(booking);

        try {
            // Step 8: Process payment using Strategy pattern
            Payment payment = processPayment(booking.getId(), totalAmount, paymentMethod, idempotencyKey);

            // Step 9: Payment success → confirm booking and seats
            booking.setPaymentId(payment.getId());
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.setPnr(generatePnr(booking.getId()));
            bookingRepository.save(booking);

            seatLockManager.confirmSeats(seats);  // BLOCKED → BOOKED
            flightSeatRepository.saveAll(seats);
            return booking;
        } catch (PaymentFailedException e) {
            // Step 10: Payment failed → rollback seats to AVAILABLE
            seatLockManager.releaseSeats(seats);
            flightSeatRepository.saveAll(seats);
            throw e;
        }
    }

    public Booking cancelBooking(String pnr)
            throws BookingNotFoundException, InvalidBookingStateException, PaymentFailedException {
        Booking booking = bookingRepository.findByPnr(pnr)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found for PNR " + pnr));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new InvalidBookingStateException("Only confirmed bookings can be cancelled");
        }

        Flight flight = flightRepository.findById(booking.getFlightId()).orElseThrow();
        if (flight.getDepartureTime().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidBookingStateException("Cannot cancel within 2 hours of departure");
        }

        Payment payment = paymentRepository.findById(booking.getPaymentId()).orElseThrow();
        PaymentStrategy strategy = PaymentStrategyFactory.getStrategy(payment.getMethod());
        if (!strategy.refund(payment.getAmount())) {
            throw new PaymentFailedException("Refund failed");
        }
        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        List<FlightSeat> seats = flightSeatRepository.findAllById(booking.getFlightSeatIds());
        seatLockManager.releaseSeats(seats);
        flightSeatRepository.saveAll(seats);

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingHistory(long passengerId) {
        return bookingRepository.findByPassengerId(passengerId);
    }

    private Payment processPayment(long bookingId, BigDecimal amount, PaymentMethod method, String idempotencyKey)
            throws PaymentFailedException {
        // Step 8a: Return existing payment if same idempotency key (retry-safe)
        if (idempotencyKey != null) {
            Optional<Payment> existing = paymentRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        // Step 8b: Get payment strategy from factory (Strategy pattern)
        PaymentStrategy strategy = PaymentStrategyFactory.getStrategy(method);
        if (!strategy.pay(amount)) {
            throw new PaymentFailedException("Payment failed for method " + method);
        }

        // Step 8c: Persist successful payment record
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setAmount(amount);
        payment.setMethod(method);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setExternalReference(method + "_" + System.currentTimeMillis());
        payment.setIdempotencyKey(idempotencyKey);
        return paymentRepository.save(payment);
    }

    private String generatePnr(long bookingId) {
        return "PNR" + String.format("%04d", bookingId);
    }
}
