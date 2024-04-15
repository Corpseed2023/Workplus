package com.example.desktime.service;

import com.example.desktime.model.Screenshot;

import java.io.IOException;

public interface ScreenShotService {


    Screenshot uploadScreenshot(Long userId, byte[] screenshotData, String userMail, String originalFilename) throws IOException;

}
