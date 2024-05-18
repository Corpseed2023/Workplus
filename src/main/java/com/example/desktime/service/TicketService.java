package com.example.desktime.service;


import com.example.desktime.requestDTO.TicketRequest;
import com.example.desktime.requestDTO.UpdateTicketRequest;
import com.example.desktime.responseDTO.TicketResponse;
import com.example.desktime.responseDTO.UpdatedResponseTicket;

import java.util.List;

public interface TicketService {
    TicketResponse raiseTicket(TicketRequest ticketRequest);

    List<TicketResponse> getAllTickets( String userMail);

    TicketResponse getTicketById(Long id);


    boolean deleteTicket(Long id);

    UpdatedResponseTicket updateTicket(Long id, UpdateTicketRequest updateTicketRequest);
}
