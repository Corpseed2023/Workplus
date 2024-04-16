package com.example.desktime.controller;

import com.example.desktime.model.Screenshot;
import com.example.desktime.responseDTO.ScreenshotResponse;
import com.example.desktime.service.ScreenShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ScreenshotController {


    @Autowired
    private ScreenShotService screenShotService;


    @PostMapping(value = "/uploadScreenShot", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadScreenshot(@RequestParam Long referenceId,
                                              @RequestPart(name = "file", required = false) MultipartFile file,
                                              @RequestParam String userMail) {
        try {
            if (file.isEmpty()) {
                return new ResponseEntity<>("Please upload a screenshot file.", HttpStatus.BAD_REQUEST);
            }
            byte[] screenshotData = file.getBytes();
            String originalFilename = file.getOriginalFilename(); // Get the original filename
            ScreenshotResponse screenshotResponse = screenShotService.uploadScreenshot(referenceId, screenshotData, userMail, originalFilename);
            return ResponseEntity.ok().body(screenshotResponse);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload screenshot.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
