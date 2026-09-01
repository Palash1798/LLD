package com.palash.bookmyshow.dto;

import com.palash.bookmyshow.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// if user is registered, give back userId as a confirmation
public class SignUpResponseDTO {
    private Long userId;
    private ResponseStatus status;
}
