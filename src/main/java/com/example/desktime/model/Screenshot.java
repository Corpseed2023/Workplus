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

    @Column(name = "screenshot_url")
    private String screenshotUrl; // Store the URL of the image

    private String screenshotName; // Store the name of the image file

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    // Constructor with necessary fields
    public Screenshot(User user, Date screenshotTime, String screenshotUrl, String screenshotName) {
        this.user = user;
        this.screenshotTime = screenshotTime;
        this.screenshotUrl = screenshotUrl;
        this.screenshotName = screenshotName;
        this.createdAt = new Date(); // Set the createdAt field to the current date/time
        this.updatedAt = new Date(); // Set the updatedAt field to the current date/time
    }
}
