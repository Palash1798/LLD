package com.palash.bookmyshow.dto;

import com.palash.bookmyshow.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookMovieResponseDTO {
    private Long bookingId;
    private ResponseStatus status;
    private  int amount;
}
