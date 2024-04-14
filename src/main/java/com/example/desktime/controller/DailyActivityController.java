package com.example.desktime.controller;

import com.example.desktime.requestDTO.DailyActivityRequest;
import com.example.desktime.responseDTO.DailyActivityResponse;
import com.example.desktime.service.DailyActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RestController
public class DailyActivityController {

    @Autowired
    private DailyActivityService dailyActivityService;


    @PostMapping("/saveDailyActivity")
    public ResponseEntity<?> saveDailyActivity(@RequestBody DailyActivityRequest request) {
        try {
            // Debug logging to check the format of the date and login time
            // Explicitly parse the date to ensure it's in the correct format
            LocalDate.parse(request.getDate()); // This line will throw DateTimeParseException if the format is incorrect

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



}
