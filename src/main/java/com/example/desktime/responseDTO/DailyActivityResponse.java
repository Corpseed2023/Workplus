package com.example.desktime.responseDTO;

import com.example.desktime.model.AttendanceType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class DailyActivityResponse {
    private Long id;
    private String userEmail;
    private LocalDate date;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private boolean present;
    private String dayOfWeek;
    private AttendanceType attendanceType;
    private LocalDateTime todayTotalTime;
    private String loginTimeConvention;
    private String logoutTimeConvention;
    private String loginTimeToLogoutTime;
    private String totalTime;
    private String gapTime ;


    // Constructors, getters, and setters


    public DailyActivityResponse() {
    }


}

