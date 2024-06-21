package com.example.desktime.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GapTrackResponse {

    private Long id;
    private Long userId;
    private LocalDate date;
    private LocalDateTime gapStartTime;
    private String reason;
    private String gapTime;
    private String workingStatus;
    private Boolean availability;



}
