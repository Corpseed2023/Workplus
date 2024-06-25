package com.example.desktime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GapTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "gap_start_time", nullable = false)
    private LocalDateTime gapStartTime;

    @Column(name = "reason")
    private String reason;

    private String workingStatus;

    private Boolean availability = false;

    private int count ;

    private boolean reasonUpdatedFlag=false;




}
