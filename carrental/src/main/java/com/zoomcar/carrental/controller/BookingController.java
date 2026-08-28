package com.zoomcar.carrental.controller;

import com.zoomcar.carrental.dto.BookCarRequestDTO;
import com.zoomcar.carrental.dto.BookCarResponseDTO;
import com.zoomcar.carrental.dto.SearchCarRequestDTO;
import com.zoomcar.carrental.dto.SearchCarResponseDTO;
import com.zoomcar.carrental.dto.enums.ResponseType;
import com.zoomcar.carrental.models.Reservation;
import com.zoomcar.carrental.services.BookingService;

import java.util.List;

public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public SearchCarResponseDTO searchCars(SearchCarRequestDTO requestDTO) {
        SearchCarResponseDTO responseDTO = new SearchCarResponseDTO();
        try {
            responseDTO.setCars(bookingService.searchAvailableCars(
                    requestDTO.getLocation(),
                    requestDTO.getStartDate(),
                    requestDTO.getEndDate()));
            responseDTO.setResponseType(ResponseType.SUCCESS);
        } catch (Exception e) {
            responseDTO.setResponseType(ResponseType.FAILURE);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }

    public BookCarResponseDTO bookCar(BookCarRequestDTO requestDTO) {
        BookCarResponseDTO responseDTO = new BookCarResponseDTO();
        try {
            Reservation reservation = bookingService.bookCar(
                    requestDTO.getCustomerId(),
                    requestDTO.getCarId(),
                    requestDTO.getStartDate(),
                    requestDTO.getEndDate(),
                    requestDTO.getPaymentMethod());
            responseDTO.setReservation(reservation);
            responseDTO.setResponseType(ResponseType.SUCCESS);
        } catch (Exception e) {
            responseDTO.setResponseType(ResponseType.FAILURE);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }
}
