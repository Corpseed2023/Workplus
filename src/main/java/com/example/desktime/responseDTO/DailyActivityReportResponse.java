package com.example.desktime.responseDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class DailyActivityReportResponse {

    private Long id;
    private String userName;
    private String userEmail;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private LocalDate date;
    private String present;
    private String totalTime;
    private String dayOfWeek;
    private String attendanceType;

}
