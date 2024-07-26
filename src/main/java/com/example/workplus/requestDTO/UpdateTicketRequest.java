package com.example.workplus.requestDTO;

import lombok.Data;

@Data
public class UpdateTicketRequest {

    private Long id;
    private String subject;
    private String description;
}
