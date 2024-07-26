package com.example.workplus.service;


import com.example.workplus.requestDTO.TicketRequest;
import com.example.workplus.requestDTO.UpdateTicketRequest;
import com.example.workplus.responseDTO.TicketResponse;
import com.example.workplus.responseDTO.UpdatedResponseTicket;

import java.util.List;

public interface TicketService {
    TicketResponse raiseTicket(TicketRequest ticketRequest);

    List<TicketResponse> getAllTickets( String userMail);

    TicketResponse getTicketById(Long id);


    boolean deleteTicket(Long id);

    UpdatedResponseTicket updateTicket(Long id, UpdateTicketRequest updateTicketRequest);
}
