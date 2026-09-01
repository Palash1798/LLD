package com.palash.bookmyshow.controllers;

import com.palash.bookmyshow.dto.LoginRequestDTO;
import com.palash.bookmyshow.dto.LoginResponseDTO;
import com.palash.bookmyshow.dto.SignUpRequestDTO;
import com.palash.bookmyshow.dto.SignUpResponseDTO;
import com.palash.bookmyshow.enums.ResponseStatus;
import com.palash.bookmyshow.models.User;
import com.palash.bookmyshow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }
    public SignUpResponseDTO signup(SignUpRequestDTO requestDTO){
        SignUpResponseDTO responseDTO = new SignUpResponseDTO();

        try{
            User registeredUser = userService.signup(
                    requestDTO.getEmail(),
                    requestDTO.getUserName(),
                    requestDTO.getPassword()
            );

            responseDTO.setUserId(registeredUser.getId());
            responseDTO.setStatus(ResponseStatus.SUCCESS);
        } catch(Exception e){
            responseDTO.setStatus(ResponseStatus.FAILURE);
        }
        return responseDTO;
    }

    public LoginResponseDTO login(LoginRequestDTO requestDTO){
        LoginResponseDTO responseDTO = new LoginResponseDTO();

        try{
            User registeredUser = userService.login(
                    requestDTO.getEmail(),
                    requestDTO.getPassword()
            );

            responseDTO.setUserId(registeredUser.getId());
            responseDTO.setStatus(ResponseStatus.SUCCESS);
        } catch(Exception e){
            responseDTO.setStatus(ResponseStatus.FAILURE);
            responseDTO.setErrorMessage(e.getMessage());
        }
        return responseDTO;
    }
}
