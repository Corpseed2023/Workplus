package com.example.desktime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_activity", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date"})})
@Getter
@Setter
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

}
