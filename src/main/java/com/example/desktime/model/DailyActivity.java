package com.example.desktime.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "daily_activities")
public class DailyActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "present")
    private boolean present;

    // Constructors, getters, and setters
    // Getters and setters omitted for brevity
    public DailyActivity() {
    }

    public DailyActivity(User user, LocalDate date, boolean present) {
        this.user = user;
        this.date = date;
        this.present = present;
    }
}
