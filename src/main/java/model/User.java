package model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "api_data_id")
    private List<ProcessData> processes;

    @Column(name = "login_time")
    private String loginTime;

    @Column(name = "logout_time")
    private String logoutTime;
}
