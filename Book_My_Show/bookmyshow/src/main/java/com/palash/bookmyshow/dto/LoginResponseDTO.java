package com.palash.bookmyshow.dto;

import com.palash.bookmyshow.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private Long userId;
    private ResponseStatus status;
    private String errorMessage;
}
