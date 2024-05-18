package com.example.desktime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "gap_start_time", nullable = false)
    private Date gapStartTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "gap_end_time", nullable = false)
    private Date gapEndTime;

    @Column(name = "reason")
    private String reason;

    private String workingStatus;

    // Getters and setters are provided by Lombok annotations
}
