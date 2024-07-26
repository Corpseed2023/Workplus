package com.example.workplus.responseDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
public class ScreenshotResponse {

    private Long id;
    private Long userId;
    private String userMail;
    private Date screenshotTime;
    private String screenshotUrl;
    private String screenshotName;
    private Date createdAt;
    private Date updatedAt;
    private LocalDate date;


    public ScreenshotResponse(Long id, Long userId, String userMail, Date screenshotTime, String screenshotUrl, LocalDate date,
                              String screenshotName, Date createdAt, Date updatedAt) {
        {
            this.id = id;
            this.userId = userId;
            this.userMail = userMail;
            this.screenshotTime = screenshotTime;
            this.screenshotUrl = screenshotUrl;
            this.screenshotName = screenshotName;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.date = date;
        }

    }
}
