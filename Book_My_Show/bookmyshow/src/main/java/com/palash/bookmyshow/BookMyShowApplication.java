package com.palash.bookmyshow;

import com.palash.bookmyshow.controllers.UserController;
import com.palash.bookmyshow.dto.LoginRequestDTO;
import com.palash.bookmyshow.dto.LoginResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing  //create events, whenever DB update happens.
@SpringBootApplication
public class BookMyShowApplication implements CommandLineRunner {
	private UserController userController;

	@Autowired
	public BookMyShowApplication(UserController userController){
		this.userController = userController;
	}


	@Override
	public void run(String... args) throws Exception {
//		SignUpRequestDTO requestDTO = new SignUpRequestDTO();
//		requestDTO.setEmail("akshay@gmail.com");
//		requestDTO.setPassword("Akshay@123");
//		requestDTO.setUserName("Akshay123");
//
//		SignUpResponseDTO responseDTO = userController.signup(requestDTO);
//		System.out.println("Status is : " + responseDTO.getStatus());
//		System.out.println("Assigned UserId is : " + responseDTO.getUserId());

		LoginRequestDTO requestDTO = new LoginRequestDTO();
		requestDTO.setEmail("akshay@gmail.com");
		requestDTO.setPassword("Akshay@123");

		LoginResponseDTO responseDTO = userController.login(requestDTO);
		System.out.println("Status is : " + responseDTO.getStatus());
		System.out.println("Logged UserId is : " + responseDTO.getUserId());
		System.out.println("Error message is : " + responseDTO.getErrorMessage());

	}

	public static void main(String[] args) {
		SpringApplication.run(BookMyShowApplication.class, args);
	}
}
