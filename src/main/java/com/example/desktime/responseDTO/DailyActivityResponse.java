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


    // Constructors, getters, and setters


    public DailyActivityResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }


    public LocalDate getDate() {

        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public LocalDateTime getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }

    public AttendanceType getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(AttendanceType attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getLoginTimeConvention() {
        return loginTimeConvention;
    }

    public void setLoginTimeConvention(String loginTimeConvention) {
        this.loginTimeConvention = loginTimeConvention;
    }

    public LocalDateTime getTodayTotalTime() {
        return todayTotalTime;
    }

    public void setTodayTotalTime(LocalDateTime todayTotalTime) {
        this.todayTotalTime = todayTotalTime;
    }

    public String getLogoutTimeConvention() {
        return logoutTimeConvention;
    }

    public void setLogoutTimeConvention(String logoutTimeConvention) {
        this.logoutTimeConvention = logoutTimeConvention;
    }

    public String getLoginTimeToLogoutTime() {
        return loginTimeToLogoutTime;
    }

    public void setLoginTimeToLogoutTime(String loginTimeToLogoutTime) {
        this.loginTimeToLogoutTime = loginTimeToLogoutTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }
}

