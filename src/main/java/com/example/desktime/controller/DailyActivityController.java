package com.example.desktime.controller;

import com.example.desktime.service.DailyActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
public class DailyActivityController {

    @Autowired
    private DailyActivityService dailyActivityService;


        @PostMapping("/saveDailyActivity")
        public ResponseEntity<String> saveDailyActivity(@RequestParam String email,
                                                        @RequestParam String date) {
            try {
                LocalDate activityDate = LocalDate.parse(date);
                dailyActivityService.saveDailyActivity(email, activityDate);
                return new ResponseEntity<>("Daily activity saved successfully!", HttpStatus.OK);
            } catch (DateTimeParseException e) {
                return new ResponseEntity<>("Invalid date format. Please provide the date in yyyy-MM-dd format.", HttpStatus.BAD_REQUEST);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

}
