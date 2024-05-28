package com.example.desktime.ApiResponse;

import com.example.desktime.responseDTO.TicketResponse;
import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorResponse {


    public static ErrorResponse userNotFound() {
        return new ErrorResponse(HttpStatus.NOT_FOUND, new Date(), "User Not Found", "User not found . Please provide user deatils");
    }


    private HttpStatus status;
    private Date timestamp;
    private String error;
    private String message;

    public ErrorResponse(HttpStatus status, Date timestamp, String error, String message) {
        this.status = status;
        this.timestamp = timestamp;
        this.error = error;
        this.message = message;
    }


    // Method to convert ErrorResponse to TicketResponse
    public TicketResponse toTicketResponse() {
        return new TicketResponse(0L, new Date(), error, message, false);
    }
}
