package com.example.desktime.responseDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class DailyActivityReportResponse {

    private Long userId;
    private String userName;
    private String userEmail;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private LocalDate date;
}
