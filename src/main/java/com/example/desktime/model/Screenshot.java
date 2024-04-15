package com.example.desktime.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "screenshot")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Screenshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "screenshot_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date screenshotTime;

    @Lob
    @Column(name = "screenshot_data", columnDefinition = "BLOB")
    private String screenshotData;

    private String screenshotName;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Screenshot(User user, Date screenshotTime, String screenshotData) {
        this.user = user;
        this.screenshotTime = screenshotTime;
        this.screenshotData = screenshotData;
    }

}
