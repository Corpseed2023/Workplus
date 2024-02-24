//package com.example.desktime.model;
//
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Data
//    public class ProcessData {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "username")
//    private String username;
//
//    @Column(name = "login_time")
//    private LocalDateTime loginTime;
//
//    @Column(name = "logout_time")
//    private LocalDateTime logoutTime;
//
//    @OneToMany(mappedBy = "userLoginRecord", cascade = CascadeType.ALL)
//    private List<SystemActivity> systemActivities;
//
//    @OneToMany(mappedBy = "userLoginRecord", cascade = CascadeType.ALL)
//    private List<SoftwareActivity> softwareActivities;
//
//
//
//}
//
//
