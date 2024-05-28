package com.example.desktime.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedResponseTicket {
    private Long id;
    private Long createdBy;
    private Date creationDate;
    private Date updatedAt;
    private String subject;
    private String description;
    private boolean issueStatus;
    private String resolution;
    private boolean isEnable;
    private Long resolutionBy;
    private Date resolutionDate;
}