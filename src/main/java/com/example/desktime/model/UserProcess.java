package com.example.desktime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserProcess {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "date")
    private LocalDate date; // Date of the process activity

    @Column(name = "process_name")
    private String processName; // Name of the process

    @Column(name = "start_time")
    private LocalDateTime startTime; // Start time of the process activity

    @Column(name = "end_time")
    private LocalDateTime endTime; // End time of the process activity

    @Column(name = "duration_minutes")
    private int durationMinutes; // Duration of the process activity in minutes

    @Column(name = "process_path")
    private String processPath; // Path of the executable file for the process

    @Column(name = "device_name")
    private String deviceName; // Name of the device where the process was run

    @Column(name = "operating_system")
    private String operatingSystem; // Operating system of the device

    @Column(name = "process_id")
    private int processId; // Unique identifier of the process

    @Column(name = "process_type")
    private String processType; // Categorization of the process into types such as "Editor", "Browser", "IDE", etc.

    @Column(name = "activity_type")
    private String activityType; // Specifies whether the activity was productive, non-productive, or neutral

    @Column(name = "additional_metadata")
    private String additionalMetadata; // Additional metadata about the process activity


    public UserProcess() {
    }


}
