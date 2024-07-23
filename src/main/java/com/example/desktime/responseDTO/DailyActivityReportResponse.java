package com.example.desktime.responseDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class DailyActivityReportResponse {

    private Long id;
    private String userName;
    private String userEmail;
    private LocalTime loginTime;
    private LocalTime logoutTime;
    private LocalDate date;
    private String present;
    private String totalTime;
    private String dayOfWeek;
    private String attendanceType;
    private String gapTime;

}
