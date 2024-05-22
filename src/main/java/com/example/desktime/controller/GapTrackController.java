package com.example.desktime.controller;


import com.example.desktime.model.GapTrack;
import com.example.desktime.requestDTO.GapTrackRequest;
import com.example.desktime.responseDTO.DailyActivityReportResponse;
import com.example.desktime.service.GapTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/gap-track")
@CrossOrigin(origins = "*", maxAge = 3600)

public class GapTrackController {

    @Autowired
    private GapTrackService gapTrackService;

    @PostMapping("/saveGap")
    public ResponseEntity<?> saveGapTrack(@RequestBody GapTrackRequest gapTrackRequest) {
        try {
            GapTrack savedGapTrack = gapTrackService.saveGapTrack(gapTrackRequest);
            return new ResponseEntity<>(savedGapTrack, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving GapTrack: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateGap")
    public ResponseEntity<?> updateGapTrack(@RequestBody GapTrackRequest gapTrackRequest) {

        if (gapTrackRequest== null)
        {
            return new ResponseEntity<>("Data Not Found " ,HttpStatus.NOT_FOUND);
        }

        try {

            GapTrack updatedGap = gapTrackService.updateGapTrack(gapTrackRequest.getStatus(),gapTrackRequest.getUserEmail(),gapTrackRequest.getDate());

            return new ResponseEntity<>(updatedGap, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving GapTrack: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @GetMapping("/gap-track")
//    public ResponseEntity<?> get(@RequestParam String email, @RequestParam int year, @RequestParam int month) {
//
//        try {
//            LocalDate startDate = LocalDate.of(year, month, 1);
//            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
//            List<DailyActivityReportResponse> response = dailyActivityService.getMonthlyActivityReport(email, startDate, endDate);
//
//            if (response == null || response.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No activity found for the specified period.");
//            }
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request: " + e.getMessage());
//        }
//    }


    @GetMapping("/gap-track")
    public ResponseEntity<?> getUserGapData(@RequestParam Long userId,@RequestParam(required = false) LocalDate date)

    {

        if (date==null)
        {
            date=LocalDate.now();
        }

       List<GapTrack> userGapList= gapTrackService.fetchUserGapData(userId,date);


        return  ResponseEntity.ok(userGapList);
    }


}

