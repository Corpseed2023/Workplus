package com.example.workplus.responseDTO.dailActivityResponse;

import com.example.workplus.model.AttendanceType;
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
    private String loginTimeConvention;
    private String gapTime;
    private String productiveTime;

    public DailyActivityResponse() {
    }


}

