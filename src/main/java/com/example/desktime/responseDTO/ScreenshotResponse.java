package com.example.desktime.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ScreenshotResponse {

    private Long id;
    private Long userId;
    private String userMail;
    private Date screenshotTime;
    private String screenshotUrl; // Change field to store the URL of the image instead of image data
    private String screenshotName;
    private Date createdAt;
    private Date updatedAt;


    public ScreenshotResponse(Long id, Long userId, String userMail, Date screenshotTime, String screenshotUrl, String screenshotName, Date createdAt, Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.userMail = userMail;
        this.screenshotTime = screenshotTime;
        this.screenshotUrl = screenshotUrl;
        this.screenshotName = screenshotName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
