package com.palash.bookmyshow.controllers;

import com.palash.bookmyshow.dto.BookMovieRequestDTO;
import com.palash.bookmyshow.dto.BookMovieResponseDTO;
import com.palash.bookmyshow.enums.ResponseStatus;
import com.palash.bookmyshow.models.Booking;
import com.palash.bookmyshow.services.BookingServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BookingController {
    private BookingServices bookingServices;

    @Autowired   //It allows Spring to automatically inject dependencies into the class, eliminating the need for manual configuration
    public BookingController(BookingServices bookingService){
        this.bookingServices = bookingService;
    }

    public BookMovieResponseDTO bookMovie(BookMovieRequestDTO requestDTO){
        BookMovieResponseDTO responseDTO = new BookMovieResponseDTO();

        try {
            Booking booking = bookingServices.bookMovie(
                    requestDTO.getUserId(),
                    requestDTO.getShowId(),
                    requestDTO.getShowSeatId()
            );

            responseDTO.setBookingId(booking.getId());
            responseDTO.setAmount(booking.getAmount());
            responseDTO.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e){
            responseDTO.setStatus(ResponseStatus.FAILURE);
        }
        return responseDTO;
    }
}
