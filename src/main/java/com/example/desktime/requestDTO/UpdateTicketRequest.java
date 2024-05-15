package com.example.desktime.requestDTO;

import lombok.Data;

@Data
public class UpdateTicketRequest {

    private Long id;
    private String subject;
    private String description;
}
