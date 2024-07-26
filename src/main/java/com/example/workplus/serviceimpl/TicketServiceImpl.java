package com.example.workplus.serviceimpl;


import com.example.workplus.ApiResponse.UserNotFoundException;
import com.example.workplus.model.Ticket;
import com.example.workplus.model.User;
import com.example.workplus.repository.TicketRepository;
import com.example.workplus.repository.UserRepository;
import com.example.workplus.requestDTO.TicketRequest;
import com.example.workplus.requestDTO.UpdateTicketRequest;
import com.example.workplus.responseDTO.TicketResponse;
import com.example.workplus.responseDTO.UpdatedResponseTicket;
import com.example.workplus.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TicketServiceImpl implements TicketService {


    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TicketResponse raiseTicket(TicketRequest ticketRequest) {
        Optional<User> userDetails = userRepository.findById(ticketRequest.getUserId());

        if (userDetails.isPresent()) {
            User user = userDetails.get();

            Ticket ticket = new Ticket();
            ticket.setSubject(ticketRequest.getSubject());
            ticket.setDescription(ticketRequest.getDescription());
            ticket.setCreateBy(user.getId());
            Ticket savedTicket = ticketRepository.save(ticket);

            return new TicketResponse(
                    savedTicket.getId(),
                    savedTicket.getCreationDate(),
                    savedTicket.getSubject(),
                    savedTicket.getDescription(),
                    savedTicket.isIssueStatus()
            );
        } else {
            throw new UserNotFoundException();
        }
    }



    @Override
    public List<TicketResponse> getAllTickets( String userMail) {

        User user = userRepository.findUserByEmail(userMail);

        List<Ticket> allTickets = ticketRepository.findAll();
        return allTickets.stream()
                .map(ticket -> new TicketResponse(
                        ticket.getId(),
                        ticket.getCreationDate(),
                        ticket.getSubject(),
                        ticket.getDescription(),
                        ticket.isIssueStatus()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public TicketResponse getTicketById(Long id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);

        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            return new TicketResponse(
                    ticket.getId(),
                    ticket.getCreationDate(),
                    ticket.getSubject(),
                    ticket.getDescription(),
                    ticket.isIssueStatus()
            );
        } else {
            return null;
        }
    }


    @Override
    public UpdatedResponseTicket updateTicket(Long id, UpdateTicketRequest updateTicketRequest) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);

        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            // Update ticket details based on updateTicketRequest
            ticket.setSubject(updateTicketRequest.getSubject());
            ticket.setDescription(updateTicketRequest.getDescription());
            // Update other fields as needed
            ticket.setUpdatedAt(new Date());
            Ticket updatedTicket = ticketRepository.save(ticket);
            return convertToUpdatedResponseTicket(updatedTicket);
        } else {
            return null; // Ticket not found
        }
    }

    // Helper method to convert Ticket entity to UpdatedResponseTicket DTO
    private UpdatedResponseTicket convertToUpdatedResponseTicket(Ticket ticket) {
        UpdatedResponseTicket updatedResponseTicket = new UpdatedResponseTicket();
        // Populate updatedResponseTicket with ticket details
        updatedResponseTicket.setId(ticket.getId());
        updatedResponseTicket.setCreatedBy(ticket.getCreatedBy());
        updatedResponseTicket.setCreationDate(ticket.getCreationDate());
        updatedResponseTicket.setUpdatedAt(ticket.getUpdatedAt());
        updatedResponseTicket.setSubject(ticket.getSubject());
        updatedResponseTicket.setDescription(ticket.getDescription());
        updatedResponseTicket.setIssueStatus(ticket.isIssueStatus());
        updatedResponseTicket.setResolution(ticket.getResolution());
        updatedResponseTicket.setEnable(ticket.isEnable());
        updatedResponseTicket.setResolutionBy(ticket.getResolutionBy());
        updatedResponseTicket.setResolutionDate(ticket.getResolutionDate());
        return updatedResponseTicket;
    }

    public boolean deleteTicket(Long id) {
        int rowsUpdated = ticketRepository.updateIsEnable(id);
        return rowsUpdated > 0;

    }

}
