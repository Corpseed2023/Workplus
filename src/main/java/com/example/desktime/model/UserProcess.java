package com.example.desktime.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_processes")
public class UserProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "process_name")
    private String processName;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;
    @Column(name = "duration_minutes")
    private int durationMinutes;


    public UserProcess() {
    }

    public UserProcess(User user, String processName, LocalDateTime startTime, LocalDateTime endTime) {
        this.user = user;
        this.processName = processName;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
