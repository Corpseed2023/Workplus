package com.example.desktime.controller;

import com.example.desktime.requestDTO.DailyActivityRequest;
import com.example.desktime.requestDTO.LogoutUpdateRequest;
import com.example.desktime.responseDTO.DailyActivityReportResponse;
import com.example.desktime.responseDTO.DailyActivityResponse;
import com.example.desktime.responseDTO.LogoutUpdateResponse;
import com.example.desktime.service.DailyActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class DailyActivityController {

    @Autowired
    private DailyActivityService dailyActivityService;


    @PostMapping("/saveDailyActivity")
    public ResponseEntity<?> saveDailyActivity(@RequestBody DailyActivityRequest request) {
        try {
            if (request.getEmail() == null || !request.getEmail().endsWith("@corpseed.com")) {
                return new ResponseEntity<>("User not found within the domain corpseed.com. Email is null or doesn't contain the specified domain.", HttpStatus.NOT_FOUND);
            }
            // No need to parse LocalDate again, it's already a LocalDate object
            DailyActivityResponse response = dailyActivityService.saveDailyActivity(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format. Please provide the date in yyyy-MM-dd format.", HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<?> getDailyActivity(@RequestParam String email , @RequestParam(required = false) LocalDate date) {
        try {
            LocalDate currentDate;
            if (date != null) {
                currentDate = date;
            } else {
                currentDate = LocalDate.now();
            }

            DailyActivityResponse dailyActivityResponse = dailyActivityService.getDailyActivityByEmail(email, currentDate);
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


    @GetMapping("/report")
//    public ResponseEntity<?> getMonthlyActivityReport(@RequestParam String email, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        public ResponseEntity<?> getMonthlyActivityReport(@RequestParam String email, @RequestParam(required = false) LocalDate date) {

            try {
            LocalDate currentDate = (date != null) ? date : LocalDate.now();
            int year = currentDate.getYear();
            int month = currentDate.getMonthValue();
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            List<DailyActivityReportResponse> response = dailyActivityService.getMonthlyActivityReport(email, startDate, endDate);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request");
        }
    }





}
