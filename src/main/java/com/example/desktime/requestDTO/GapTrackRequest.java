package com.example.desktime.requestDTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GapTrackRequest {

//    private LocalDateTime dateTime;

    private String status;

    private String userEmail;

}
