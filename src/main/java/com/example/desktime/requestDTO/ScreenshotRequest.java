package com.example.desktime.requestDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreenshotRequest {

    private Long userId;
    private Long userMail;
    private Date screenshotTime;
    private String screenshotData;


}
