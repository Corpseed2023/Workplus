package com.example.desktime.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "software_activities")
public class SoftwareActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "software_name")
    private String softwareName;

    @Column(name = "activity")
    private String activity;

    @Column(name = "activity_time")
    private LocalDateTime activityTime;

    @ManyToOne
    @JoinColumn(name = "user_login_record_id")
    private User user ;


}

