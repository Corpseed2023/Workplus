package com.example.desktime.responseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreenShotAllResponse {
    private Long id;
    private String userEmail;
    private LocalDate date;
    private String screenshotTime;
    private String screenshotUrl;
    private String screenshotName;
}

