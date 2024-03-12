//package com.example.desktime.model;
//
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Table(name = "system_activities")
//public class SystemActivity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "activity")
//    private String activity;
//
//    @Column(name = "activity_time")
//    private LocalDateTime activityTime;
//
//    @ManyToOne
//    @JoinColumn(name = "user_login_record_id")
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "software_activity_id")
//    private SoftwareActivity softwareActivity;
//
//    @OneToMany(mappedBy = "systemActivity", cascade = CascadeType.ALL)
//    private List<ScreenShot> screenShots;
//
//
//}
