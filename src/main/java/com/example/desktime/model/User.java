package com.example.desktime.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name="login_date")
    private Date loginDate;

    @Column(name = "login_time")
    private LocalDateTime loginTime;

    @Column(name = "login_time_convention")
    private String loginTimeConvention;


    @Column(name = "logout_time")
    private LocalDateTime logoutTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SystemActivity> systemActivities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SoftwareActivity> softwareActivities;


}






