package com.example.desktime.controller;

import com.example.desktime.responseDTO.ScreenShotAllResponse;
import com.example.desktime.responseDTO.ScreenshotResponse;
import com.example.desktime.service.ScreenShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ScreenshotController {


    @Autowired
    private ScreenShotService screenShotService;


    @PostMapping(value = "/uploadScreenShotV2", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadScreenshot(@RequestPart(name = "file", required = false) MultipartFile file,
                                              @RequestParam(required = false) String userMail) {
        try {
            if (file.isEmpty()) {
                return new ResponseEntity<>("Please upload a screenshot file.", HttpStatus.BAD_REQUEST);
            }
            byte[] screenshotData = file.getBytes();
            String originalFilename = file.getOriginalFilename(); // Get the original filename
            ScreenshotResponse screenshotResponse = screenShotService.uploadScreenshot(screenshotData, userMail, originalFilename);
            return ResponseEntity.ok().body(screenshotResponse);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload screenshot.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUserScreenshots")
    public ResponseEntity<?> getUserScreenshots(@RequestParam String userEmail, @RequestParam(required = false) String date) {
        try {
            LocalDate screenshotDate;
            if (date != null) {
                screenshotDate = LocalDate.parse(date);
            } else {
                screenshotDate = LocalDate.now();
            }
            List<ScreenShotAllResponse> userScreenshots = screenShotService.getUserScreenshotsByEmailAndDate(userEmail, screenshotDate);
            return new ResponseEntity<>(userScreenshots, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteScreenshot")
    public ResponseEntity<?> deleteScreenshot(@RequestParam Long screenshotId,@RequestParam Long userId) {
        try {
            screenShotService.deleteScreenshotById(screenshotId,userId);
            return new ResponseEntity<>("Screenshot deleted successfully.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/uploadScreenShot", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadScreenshotV2(@RequestPart(name = "file", required = false) MultipartFile file,
                                                @RequestParam(required = false) String userMail) {
        try {
            if (file.isEmpty()) {
                return new ResponseEntity<>("Please upload a screenshot file.", HttpStatus.BAD_REQUEST);
            }
            byte[] screenshotData = file.getBytes();
            String originalFilename = file.getOriginalFilename(); // Get the original filename
            ScreenshotResponse screenshotResponse = screenShotService.uploadScreenshotV2(file, userMail, originalFilename);
            return ResponseEntity.ok().body(screenshotResponse);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload screenshot.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
