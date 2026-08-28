package com.zoomcar.carrental.services;

import com.zoomcar.carrental.enums.CarStatus;
import com.zoomcar.carrental.enums.PaymentMethod;
import com.zoomcar.carrental.enums.PaymentStatus;
import com.zoomcar.carrental.enums.ReservationStatus;
import com.zoomcar.carrental.exceptions.CarNotAvailableException;
import com.zoomcar.carrental.exceptions.CarNotFoundException;
import com.zoomcar.carrental.exceptions.CustomerNotFoundException;
import com.zoomcar.carrental.exceptions.PaymentFailedException;
import com.zoomcar.carrental.factories.PaymentStrategyFactory;
import com.zoomcar.carrental.models.Car;
import com.zoomcar.carrental.models.Customer;
import com.zoomcar.carrental.models.Payment;
import com.zoomcar.carrental.models.Reservation;
import com.zoomcar.carrental.paymentStrategies.PaymentStrategy;
import com.zoomcar.carrental.repository.CarRepository;
import com.zoomcar.carrental.repository.CustomerRepository;
import com.zoomcar.carrental.repository.PaymentRepository;
import com.zoomcar.carrental.repository.ReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingService {

    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    public BookingService(CarRepository carRepository,
                          CustomerRepository customerRepository,
                          ReservationRepository reservationRepository,
                          PaymentRepository paymentRepository) {
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.reservationRepository = reservationRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<Car> searchAvailableCars(String location, LocalDate startDate, LocalDate endDate) {
        List<Car> availableCars = new ArrayList<>();
        for (Car car : carRepository.findCarsByLocation(location)) {
            if (isCarAvailable(car.getId(), startDate, endDate)) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    public Reservation bookCar(long customerId, long carId, LocalDate startDate, LocalDate endDate,
                               PaymentMethod paymentMethod)
            throws CustomerNotFoundException, CarNotFoundException, CarNotAvailableException, PaymentFailedException {

        Optional<Customer> customerOptional = customerRepository.findCustomerById(customerId);
        if (customerOptional.isEmpty()) {
            throw new CustomerNotFoundException("Customer not found for id " + customerId);
        }

        Optional<Car> carOptional = carRepository.findCarById(carId);
        if (carOptional.isEmpty()) {
            throw new CarNotFoundException("Car not found for id " + carId);
        }

        if (!isCarAvailable(carId, startDate, endDate)) {
            throw new CarNotAvailableException("Car not available for selected dates");
        }

        Car car = carOptional.get();
        BigDecimal totalAmount = calculateTotalAmount(car.getDailyRate(), startDate, endDate);

        Reservation reservation = new Reservation();
        reservation.setCustomerId(customerId);
        reservation.setCarId(carId);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setTotalAmount(totalAmount);
        reservation.setStatus(ReservationStatus.CONFIRMED);

        Payment payment = processPayment(paymentMethod, totalAmount);
        paymentRepository.savePayment(payment);

        reservation.setPaymentId(payment.getId());
        reservation = reservationRepository.saveReservation(reservation);
        reservation.setReservationNumber("RES_" + reservation.getId());

        car.setStatus(CarStatus.RESERVED);
        carRepository.saveCar(car);

        return reservation;
    }

    private Payment processPayment(PaymentMethod paymentMethod, BigDecimal amount) throws PaymentFailedException {
        PaymentStrategy paymentStrategy = PaymentStrategyFactory.getPaymentStrategyByPayMethod(paymentMethod);
        boolean paymentSuccess = paymentStrategy.pay(amount);
        if (!paymentSuccess) {
            throw new PaymentFailedException("Payment failed for method " + paymentMethod);
        }

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setMethod(paymentMethod);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setExternalReference(paymentMethod + "_" + System.currentTimeMillis());
        return payment;
    }

    private boolean isCarAvailable(long carId, LocalDate startDate, LocalDate endDate) {
        for (Reservation reservation : reservationRepository.findReservationsByCarId(carId)) {
            if (reservation.getStatus() == ReservationStatus.CANCELLED) {
                continue;
            }
            if (hasOverlap(startDate, endDate, reservation.getStartDate(), reservation.getEndDate())) {
                return false;
            }
        }
        return true;
    }

    static boolean hasOverlap(LocalDate start, LocalDate end, LocalDate existingStart, LocalDate existingEnd) {
        return start.isBefore(existingEnd) && existingStart.isBefore(end);
    }

    private BigDecimal calculateTotalAmount(BigDecimal dailyRate, LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days <= 0) {
            days = 1;
        }
        return dailyRate.multiply(BigDecimal.valueOf(days));
    }
}
