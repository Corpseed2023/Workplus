package com.example.desktime.controller;


import com.example.desktime.ApiResponse.ErrorResponse;
import com.example.desktime.ApiResponse.UserNotFoundException;
import com.example.desktime.requestDTO.TicketRequest;
import com.example.desktime.requestDTO.UpdateTicketRequest;
import com.example.desktime.responseDTO.TicketResponse;
import com.example.desktime.responseDTO.UpdatedResponseTicket;
import com.example.desktime.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/raise")
    public ResponseEntity<TicketResponse> raiseTicket(@Valid @RequestBody TicketRequest ticketRequest) {
        try {
            TicketResponse ticketResponse = ticketService.raiseTicket(ticketRequest);
            return ResponseEntity.ok(ticketResponse);
        } catch (UserNotFoundException e) {

            ErrorResponse errorResponse = ErrorResponse.userNotFound();

            TicketResponse ticketResponse = errorResponse.toTicketResponse();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ticketResponse);
        } catch (Exception e) {
            // Handle other exceptions and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TicketResponse(0L, new Date(), "Error", e.getMessage(), false));
        }
    }




    @GetMapping("/allTickets")
    public ResponseEntity<List<TicketResponse>> getAllTickets(@RequestParam String userMail) {
        List<TicketResponse> allTickets = ticketService.getAllTickets(userMail);
        return ResponseEntity.ok(allTickets);
    }

    @GetMapping("/ticket")
    public ResponseEntity<TicketResponse> getTicketById(@RequestParam Long id) {
        try {
            TicketResponse ticket = ticketService.getTicketById(id);

            if (ticket != null) {
                return ResponseEntity.ok(ticket);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Handle exception and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TicketResponse(0L, new Date(), "Error", e.getMessage(), false));
        }
    }


    @PutMapping("/updateTicket")
    public ResponseEntity<UpdatedResponseTicket> updateTicket(
            @RequestParam Long id,
            @Valid @RequestBody UpdateTicketRequest updateTicketRequest) {
        try {
            UpdatedResponseTicket updatedTicket = ticketService.updateTicket(id, updateTicketRequest);

            if (updatedTicket != null) {
                return ResponseEntity.ok(updatedTicket);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Handle exception and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UpdatedResponseTicket(0L, null, null, null, null, null, false, null, false, null, null));
        }
    }


    @DeleteMapping("/deleteTicket")
    public boolean deleteTicket(@RequestParam Long id) {

        boolean isDeleted = ticketService.deleteTicket(id);

        return isDeleted;
    }

}