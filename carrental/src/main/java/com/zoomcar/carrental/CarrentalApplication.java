package com.zoomcar.carrental;

import com.zoomcar.carrental.controller.BookingController;
import com.zoomcar.carrental.enums.CarCategory;
import com.zoomcar.carrental.enums.CarStatus;
import com.zoomcar.carrental.enums.PaymentMethod;
import com.zoomcar.carrental.models.Car;
import com.zoomcar.carrental.models.Customer;
import com.zoomcar.carrental.repository.CarRepository;
import com.zoomcar.carrental.repository.CustomerRepository;
import com.zoomcar.carrental.repository.PaymentRepository;
import com.zoomcar.carrental.repository.ReservationRepository;
import com.zoomcar.carrental.services.BookingService;

import java.math.BigDecimal;

public class CarRentalApplication {

    public static CarRepository carRepository;
    public static CustomerRepository customerRepository;

    public static void main(String[] args) {
        // controller -> service -> repository (manual wiring, same as ParkingLot)
        CarRepository carRepository = new CarRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        ReservationRepository reservationRepository = new ReservationRepository();
        PaymentRepository paymentRepository = new PaymentRepository();

        BookingService bookingService = new BookingService(
                carRepository,
                customerRepository,
                reservationRepository,
                paymentRepository);

        BookingController bookingController = new BookingController(bookingService);

        CarRentalApplication.carRepository = carRepository;
        CarRentalApplication.customerRepository = customerRepository;
        initialiseDatabase();

        Client client = new Client(bookingController);
        client.runTestCases();
    }

    private static void initialiseDatabase() {
        Customer customer = new Customer();
        customer.setName("Rahul Sharma");
        customer.setEmail("rahul@example.com");
        customerRepository.saveCustomer(customer);

        Car swift = new Car();
        swift.setModel("Swift");
        swift.setRegistrationNumber("MH-01-AB-1234");
        swift.setCategory(CarCategory.HATCHBACK);
        swift.setDailyRate(new BigDecimal("1500"));
        swift.setLocation("Mumbai");
        swift.setStatus(CarStatus.AVAILABLE);
        carRepository.saveCar(swift);

        Car city = new Car();
        city.setModel("City");
        city.setRegistrationNumber("MH-01-CD-5678");
        city.setCategory(CarCategory.SEDAN);
        city.setDailyRate(new BigDecimal("2200"));
        city.setLocation("Mumbai");
        city.setStatus(CarStatus.AVAILABLE);
        carRepository.saveCar(city);
    }
}
