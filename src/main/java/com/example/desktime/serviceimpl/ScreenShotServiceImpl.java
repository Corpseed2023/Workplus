package com.example.desktime.serviceimpl;


import com.example.desktime.model.Screenshot;
import com.example.desktime.model.User;
import com.example.desktime.repository.ScreenshotRepository;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.responseDTO.ScreenshotResponse;
import com.example.desktime.service.ScreenShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ScreenShotServiceImpl implements ScreenShotService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScreenshotRepository screenshotRepository;


    @Override
    public Screenshot uploadScreenshot(Long userId, byte[] screenshotData, String userMail, String originalFilename) throws IOException {
        User user = userRepository.findUserByEmail(userMail);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + userMail);
        }

        // Save the screenshot file on the computer drive
        String directoryPath = "D:/DeskTimeSnap/uploadedScreenshots"; // Update the directory path as needed
        Files.createDirectories(Paths.get(directoryPath));

        // Append a timestamp to avoid filename conflicts
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = userId + "_" + timestamp + "_" + originalFilename; // Use the original filename
        Path filePath = Paths.get(directoryPath, fileName);
        Files.write(filePath, screenshotData);

        // Create the Screenshot entity with the original file name
        Screenshot screenshot = new Screenshot(user, new Date(), fileName);
        screenshot.setScreenshotName(fileName); // Set the screenshot name
        screenshot = screenshotRepository.save(screenshot);

        return screenshot;
    }

}
