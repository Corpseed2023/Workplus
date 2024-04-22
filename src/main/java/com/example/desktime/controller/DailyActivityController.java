package com.example.desktime.controller;

import com.example.desktime.requestDTO.DailyActivityRequest;
import com.example.desktime.requestDTO.LogoutUpdateRequest;
import com.example.desktime.responseDTO.DailyActivityResponse;
import com.example.desktime.responseDTO.LogoutUpdateResponse;
import com.example.desktime.service.DailyActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class DailyActivityController {

    @Autowired
    private DailyActivityService dailyActivityService;


    @PostMapping("/saveDailyActivity")
    public ResponseEntity<?> saveDailyActivity(@RequestBody DailyActivityRequest request) {
        try {
            // No need to parse LocalDate again, it's already a LocalDate object
            DailyActivityResponse response = dailyActivityService.saveDailyActivity(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format. Please provide the date in yyyy-MM-dd format.", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//Suppose user not using system then this API will run to update the logout time of user which the latest one by Windows Scheduler
    @PatchMapping("/updateLogoutTime")
    public ResponseEntity<?> updateLogoutTime(@RequestBody LogoutUpdateRequest request) {
        try {
            LogoutUpdateResponse response = dailyActivityService.updateLogoutTime(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/dailyActivity")
    public ResponseEntity<?> getDailyActivity(@RequestParam String email) {
        try {

            LocalDate currentDate = LocalDate.now();

            DailyActivityResponse dailyActivityResponse = dailyActivityService.getDailyActivityByEmail(email,currentDate);
            return new ResponseEntity<>(dailyActivityResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/filteredDailyActivityData")
    public ResponseEntity<?> getFilteredDailyActivityData(@RequestParam String email ,@RequestParam LocalDate date) {
        try {


            DailyActivityResponse dailyActivityResponse = dailyActivityService.getDailyActivityByEmail(email,date);
            return new ResponseEntity<>(dailyActivityResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
