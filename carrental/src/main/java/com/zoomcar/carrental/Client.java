package com.zoomcar.carrental;

import com.zoomcar.carrental.controller.BookingController;
import com.zoomcar.carrental.dto.BookCarRequestDTO;
import com.zoomcar.carrental.dto.BookCarResponseDTO;
import com.zoomcar.carrental.dto.SearchCarRequestDTO;
import com.zoomcar.carrental.dto.SearchCarResponseDTO;
import com.zoomcar.carrental.dto.enums.ResponseType;
import com.zoomcar.carrental.enums.PaymentMethod;
import com.zoomcar.carrental.models.Car;
import com.zoomcar.carrental.models.Customer;

import java.time.LocalDate;

public class Client {

    private final BookingController bookingController;

    public Client(BookingController bookingController) {
        this.bookingController = bookingController;
    }

    public void runTestCases() {
        testCase1_searchAndBook();
        testCase2_overlappingBookingShouldFail();
    }

    private void testCase1_searchAndBook() {
        SearchCarRequestDTO searchRequest = new SearchCarRequestDTO();
        searchRequest.setLocation("Mumbai");
        searchRequest.setStartDate(LocalDate.of(2026, 9, 1));
        searchRequest.setEndDate(LocalDate.of(2026, 9, 5));

        SearchCarResponseDTO searchResponse = bookingController.searchCars(searchRequest);
        System.out.println("Search status: " + searchResponse.getResponseType());
        System.out.println("Available cars: " + searchResponse.getCars().size());

        Customer customer = CarRentalApplication.customerRepository.findCustomerById(1L).orElseThrow();
        Car car = searchResponse.getCars().get(0);

        BookCarRequestDTO bookRequest = new BookCarRequestDTO();
        bookRequest.setCustomerId(customer.getId());
        bookRequest.setCarId(car.getId());
        bookRequest.setStartDate(LocalDate.of(2026, 9, 1));
        bookRequest.setEndDate(LocalDate.of(2026, 9, 5));
        bookRequest.setPaymentMethod(PaymentMethod.UPI);

        BookCarResponseDTO bookResponse = bookingController.bookCar(bookRequest);
        System.out.println("Book status: " + bookResponse.getResponseType());
        if (bookResponse.getResponseType() == ResponseType.SUCCESS) {
            System.out.println("Reservation: " + bookResponse.getReservation().getReservationNumber());
            System.out.println("Amount: " + bookResponse.getReservation().getTotalAmount());
        }
    }

    private void testCase2_overlappingBookingShouldFail() {
        Customer customer = CarRentalApplication.customerRepository.findCustomerById(1L).orElseThrow();
        Car car = CarRentalApplication.carRepository.findCarById(1L).orElseThrow();

        BookCarRequestDTO bookRequest = new BookCarRequestDTO();
        bookRequest.setCustomerId(customer.getId());
        bookRequest.setCarId(car.getId());
        bookRequest.setStartDate(LocalDate.of(2026, 9, 3));
        bookRequest.setEndDate(LocalDate.of(2026, 9, 7));
        bookRequest.setPaymentMethod(PaymentMethod.UPI);

        BookCarResponseDTO bookResponse = bookingController.bookCar(bookRequest);
        System.out.println("Overlap book status: " + bookResponse.getResponseType());
        System.out.println("Message: " + bookResponse.getMessage());
    }
}
