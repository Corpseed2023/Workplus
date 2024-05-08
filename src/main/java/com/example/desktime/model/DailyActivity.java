package com.example.desktime.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class DailyActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "login_time")
    private LocalDateTime loginTime;

    @Column(name = "logout_time")
    private LocalDateTime logoutTime;

    @Column(name = "logout_time_convention")
    private String logoutTimeConvention;

    @Column(name = "present")
    private boolean present;

    @Column(name = "day_of_week")
    private String dayOfWeek; // Store the name of the day of the week

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_type")
    private AttendanceType attendanceType;

    @Column(name = "login_time_convention")
    private String loginTimeConvention;

    private String ipAddress;

    private LocalDateTime lastSendReportTime;


    // Constructors, getters, and setters
    // Getters and setters omitted for brevity

    public DailyActivity(User user, LocalDate date, LocalDateTime loginTime, LocalDateTime logoutTime, boolean present, AttendanceType attendanceType) {
        this.user = user;
        this.date = date;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.present = present;
        this.dayOfWeek = date.getDayOfWeek().toString(); // Get the name of the day of the week
        this.attendanceType = attendanceType;
    }


    public DailyActivity() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLogoutTimeConvention() {
        return logoutTimeConvention;
    }

    public void setLogoutTimeConvention(String logoutTimeConvention) {
        this.logoutTimeConvention = logoutTimeConvention;
    }

    public LocalDateTime getLastSendReportTime() {
        return lastSendReportTime;
    }

    public void setLastSendReportTime(LocalDateTime lastSendReportTime) {
        this.lastSendReportTime = lastSendReportTime;
    }
}
