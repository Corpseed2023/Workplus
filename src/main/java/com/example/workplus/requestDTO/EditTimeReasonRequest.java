package com.example.workplus.requestDTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EditTimeReasonRequest {

    private LocalDateTime startTime;

    private LocalDateTime lastTime;

    private LocalDate date;

    private String reason;
}
