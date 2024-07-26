package com.example.workplus.responseDTO;

import lombok.Data;
import java.util.Date;

@Data
public class TicketResponse {


    private Long id;
    private Date creationDate;
    private String subject;
    private String description;
    private boolean status;


    public TicketResponse(Long id, Date creationDate, String subject, String description, boolean status) {
        this.id = id;
        this.creationDate = creationDate;
        this.subject = subject;
        this.description = description;
        this.status = status;
    }
}
