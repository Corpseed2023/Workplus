package com.example.workplus.responseDTO.userResponse;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class UserProcessResponse {

    private Long id;
    private Long userId;
    private  String userMail;
    private LocalDate date;
    private String processName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int durationMinutes;
    private String processPath;
    private String deviceName;
    private String operatingSystem;
    private int processId;
    private String processType;
    private String activityType;
    private String additionalMetadata;
}
