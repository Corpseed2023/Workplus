package com.example.desktime.service;

import com.example.desktime.model.Screenshot;
import com.example.desktime.responseDTO.ScreenshotResponse;

import java.io.IOException;

public interface ScreenShotService {


    ScreenshotResponse uploadScreenshot(Long referenceId, byte[] screenshotData, String userMail, String originalFilename) throws IOException;
}
