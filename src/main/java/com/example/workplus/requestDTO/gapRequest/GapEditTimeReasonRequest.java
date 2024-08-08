package com.example.workplus.requestDTO.gapRequest;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class GapEditTimeReasonRequest {

    private LocalDateTime startTime;

    private LocalDateTime lastTime;

    private LocalDate date;

    private String reason;
}
