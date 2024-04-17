package com.example.desktime.service;

import com.example.desktime.model.Screenshot;
import com.example.desktime.responseDTO.ScreenshotResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ScreenShotService {


    ScreenshotResponse uploadScreenshot(Long referenceId, byte[] screenshotData, String userMail, String originalFilename) throws IOException;

    List<ScreenshotResponse> getUserScreenshotsByEmailAndDate(String userEmail, LocalDate screenshotDate);

    void deleteScreenshotById(Long screenshotId, Long userId);
}
