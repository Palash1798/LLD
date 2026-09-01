package com.palash.parkinglot;

import com.palash.parkinglot.controller.TicketController;
import com.palash.parkinglot.dto.IssueTicketRequestDTO;
import com.palash.parkinglot.dto.IssueTicketResponseDTO;
import com.palash.parkinglot.dto.enums.ResponseType;
import com.palash.parkinglot.enums.VehicleType;

public class Client {
    TicketController ticketController;

    public Client(TicketController ticketController){
        this.ticketController = ticketController;
    }

    public void generateIncomingRequests(){
        testCase1();
        testCase2();
    }

    private void testCase1(){
        // 1.
        IssueTicketRequestDTO requestDTO = new IssueTicketRequestDTO();
        requestDTO.setVehicleNumber("MH03AZ7775");
        requestDTO.setGateId(1L);
        requestDTO.setVehicleOwnerName("Palash Khobragade");
        requestDTO.setVehicleType(VehicleType.CAR);

        IssueTicketResponseDTO responseDTO = ticketController.issueTicket(requestDTO);


        System.out.println("Status : " + responseDTO.getResponseType());
        System.out.println("TicketGenerated : " + responseDTO.getTicket().getTicketNumber());
    }

    private void testCase2(){
        // 2.
        IssueTicketRequestDTO requestDTO = new IssueTicketRequestDTO();
        requestDTO.setVehicleNumber("MH03AZ7775");
        requestDTO.setGateId(1L);
        requestDTO.setVehicleOwnerName("Palash Khobragade");
        requestDTO.setVehicleType(VehicleType.CAR);

        IssueTicketResponseDTO responseDTO = ticketController.issueTicket(requestDTO);


        System.out.println("Status : " + responseDTO.getResponseType());
        System.out.println("TicketGenerated : " + responseDTO.getTicket().getTicketNumber());
    }
}
