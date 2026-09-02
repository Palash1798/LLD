package com.airline.airline;

import com.airline.airline.controller.BookingController;
import com.airline.airline.enums.FlightSeatStatus;
import com.airline.airline.enums.FlightStatus;
import com.airline.airline.enums.SeatClass;
import com.airline.airline.models.Aircraft;
import com.airline.airline.models.Airport;
import com.airline.airline.models.Flight;
import com.airline.airline.models.FlightSeat;
import com.airline.airline.models.Passenger;
import com.airline.airline.repository.AirportRepository;
import com.airline.airline.repository.BoardingPassRepository;
import com.airline.airline.repository.BookingRepository;
import com.airline.airline.repository.FlightRepository;
import com.airline.airline.repository.FlightSeatRepository;
import com.airline.airline.repository.PassengerRepository;
import com.airline.airline.repository.PaymentRepository;
import com.airline.airline.services.BookingService;
import com.airline.airline.services.CheckInService;
import com.airline.airline.services.FareCalculatorService;
import com.airline.airline.services.FlightSearchService;
import com.airline.airline.services.SeatLockManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AirlineApplication {

    public static PassengerRepository passengerRepository;
    public static FlightRepository flightRepository;
    public static FlightSeatRepository flightSeatRepository;

    public static void main(String[] args) {
        AirportRepository airportRepository = new AirportRepository();
        flightRepository = new FlightRepository();
        flightSeatRepository = new FlightSeatRepository();
        passengerRepository = new PassengerRepository();
        BookingRepository bookingRepository = new BookingRepository();
        PaymentRepository paymentRepository = new PaymentRepository();
        BoardingPassRepository boardingPassRepository = new BoardingPassRepository();

        SeatLockManager seatLockManager = new SeatLockManager();
        FareCalculatorService fareCalculatorService = new FareCalculatorService();

        FlightSearchService flightSearchService = new FlightSearchService(flightRepository, airportRepository);
        BookingService bookingService = new BookingService(
                flightRepository,
                flightSeatRepository,
                passengerRepository,
                bookingRepository,
                paymentRepository,
                seatLockManager,
                fareCalculatorService);
        CheckInService checkInService = new CheckInService(
                bookingRepository,
                flightRepository,
                flightSeatRepository,
                boardingPassRepository);

        BookingController bookingController = new BookingController(
                flightSearchService, bookingService, checkInService);

        initialiseDatabase(airportRepository);

        Client client = new Client(bookingController);
        client.runTestCases();
    }

    private static void initialiseDatabase(AirportRepository airportRepository) {
        Airport del = new Airport();
        del.setCode("DEL");
        del.setName("Indira Gandhi International");
        del.setCity("Delhi");
        airportRepository.save(del);

        Airport bom = new Airport();
        bom.setCode("BOM");
        bom.setName("Chhatrapati Shivaji Maharaj International");
        bom.setCity("Mumbai");
        airportRepository.save(bom);

        Aircraft aircraft = new Aircraft();
        aircraft.setModel("Airbus A320");
        aircraft.setTotalSeats(6);

        Passenger passenger1 = new Passenger();
        passenger1.setName("Rahul Sharma");
        passenger1.setEmail("rahul@example.com");
        passenger1.setPhone("9876543210");
        passengerRepository.save(passenger1);

        Passenger passenger2 = new Passenger();
        passenger2.setName("Priya Singh");
        passenger2.setEmail("priya@example.com");
        passenger2.setPhone("9876543211");
        passengerRepository.save(passenger2);

        // Departure in 12 hours so check-in window (24h before) is open
        LocalDateTime departure = LocalDateTime.now().plusHours(12);
        LocalDateTime arrival = departure.plusHours(2);

        Flight flight = new Flight();
        flight.setFlightNumber("AI-202");
        flight.setSourceAirportId(del.getId());
        flight.setDestAirportId(bom.getId());
        flight.setAircraftId(aircraft.getId());
        flight.setDepartureTime(departure);
        flight.setArrivalTime(arrival);
        flight.setBaseFareEconomy(new BigDecimal("5000"));
        flight.setStatus(FlightStatus.SCHEDULED);
        flightRepository.save(flight);

        createSeat(flight.getId(), "12A", SeatClass.ECONOMY);
        createSeat(flight.getId(), "12B", SeatClass.ECONOMY);
        createSeat(flight.getId(), "14C", SeatClass.ECONOMY);
        createSeat(flight.getId(), "1A", SeatClass.BUSINESS);
        createSeat(flight.getId(), "1B", SeatClass.BUSINESS);
        createSeat(flight.getId(), "2A", SeatClass.FIRST);
    }

    private static void createSeat(long flightId, String seatNumber, SeatClass seatClass) {
        FlightSeat seat = new FlightSeat();
        seat.setFlightId(flightId);
        seat.setSeatNumber(seatNumber);
        seat.setSeatClass(seatClass);
        seat.setStatus(FlightSeatStatus.AVAILABLE);
        flightSeatRepository.save(seat);
    }
}
