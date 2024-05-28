package com.example.desktime.requestDTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class GapTrackRequest {

    private LocalDate date;

    private String status;

    private String userEmail;

}
