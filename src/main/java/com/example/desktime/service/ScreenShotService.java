package com.example.desktime.service;

import com.example.desktime.model.Screenshot;
import com.example.desktime.responseDTO.ScreenShotAllResponse;
import com.example.desktime.responseDTO.ScreenshotResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ScreenShotService {


    ScreenshotResponse uploadScreenshot(byte[] screenshotData, String userMail, String originalFilename) throws IOException;

    List<ScreenShotAllResponse> getUserScreenshotsByEmailAndDate(String userEmail, LocalDate screenshotDate);

    void deleteScreenshotById(Long screenshotId, Long userId);

//    ScreenshotResponse uploadScreenshotV2(MultipartFile file, String userMail, String originalFilename);
}
