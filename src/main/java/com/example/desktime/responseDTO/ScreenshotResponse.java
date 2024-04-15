package com.example.desktime.responseDTO;

import com.example.desktime.model.Screenshot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreenshotResponse {

    private Long id;
    private Long userId;
    private String userMail;
    private Date screenshotTime;
    private String screenshotData;
    private String screenshotName;

    private Date createdAt;
    private Date updatedAt;

    public ScreenshotResponse(Screenshot screenshot) {
        this.id = screenshot.getId();
        this.userId = screenshot.getUser().getId();
        this.screenshotTime = screenshot.getScreenshotTime();
        this.screenshotData = screenshot.getScreenshotData();
        this.createdAt = screenshot.getCreatedAt();
        this.updatedAt = screenshot.getUpdatedAt();
    }
}
