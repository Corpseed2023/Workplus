package com.example.workplus.controller;


import com.example.workplus.ApiResponse.UserNotFoundException;
import com.example.workplus.requestDTO.gapRequest.GapEditTimeReasonRequest;
import com.example.workplus.requestDTO.gapRequest.GapReasonRequest;
import com.example.workplus.requestDTO.gapRequest.GapTrackRequest;
import com.example.workplus.responseDTO.gapResponse.GapEditTimeResponse;
import com.example.workplus.responseDTO.gapResponse.GapTrackSaveResponse;
import com.example.workplus.responseDTO.gapResponse.GapUserResponse;
import com.example.workplus.service.GapTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api")

public class GapTrackController {

    @Autowired
    private GapTrackService gapTrackService;

    @PostMapping("/gap-track/saveGap")
    public ResponseEntity<?> saveGapTrack(@RequestBody GapTrackRequest gapTrackRequest) {
        try {
            GapTrackSaveResponse response = gapTrackService.saveGapTrack(gapTrackRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving GapTrack: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/editReason")
    public ResponseEntity<String> updateReason(@RequestParam String userEmail, @RequestParam Long lastOfflineId, @RequestParam Long lastOnlineId,
                                               @RequestParam LocalDate date,
                                               @RequestBody GapReasonRequest gapReason) {
        try {
            if (lastOfflineId != null) {
                gapTrackService.updateUserGapReason(userEmail, lastOfflineId, gapReason.getReason(),lastOnlineId);
                return ResponseEntity.ok("Gap reason updated successfully.");
            } else {
                return ResponseEntity.badRequest().body("Invalid input data.");
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the gap reason.");
        }
    }

    @PutMapping("/editTimeReason")
    public ResponseEntity<String> updateTimeReason(@RequestBody GapEditTimeReasonRequest request, @RequestParam String userEmail) {
        try {
            // Extract values from the request body
            LocalDateTime startTime = request.getStartTime();
            LocalDateTime lastTime = request.getLastTime();
            String reason = request.getReason();
            LocalDate date = request.getDate();

            if (lastTime != null) {
                gapTrackService.updateTimeUserGapReason(userEmail, startTime, reason, lastTime,date);
                return ResponseEntity.ok("Gap reason updated successfully.");
            } else {
                return ResponseEntity.badRequest().body("Invalid input data.");
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the gap reason.");
        }
    }
    @DeleteMapping("/deleteGap")
    public ResponseEntity<String> removeGap(@RequestParam String userEmail, @RequestParam Long lastOfflineId, @RequestParam Long lastOnlineId,
                                            @RequestParam LocalDate date,
                                            @RequestBody GapReasonRequest gapReason) {
        try {
            if (lastOfflineId != null) {
                gapTrackService.removeGap(userEmail, lastOfflineId, gapReason.getReason(),lastOnlineId);
                return ResponseEntity.ok("Gap Removed Successfully");
            } else {
                return ResponseEntity.badRequest().body("Invalid input data.");
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the gap reason.");
        }
    }



    @GetMapping("/gapActivity")
    public ResponseEntity<GapUserResponse> getUserActivity(
            @RequestParam("email") String userEmail, @RequestParam(required = false) LocalDate date) {
        try {
            LocalDate currentDate = (date != null) ? date : LocalDate.now();

            GapUserResponse response = gapTrackService.getUserActivity(userEmail, currentDate);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/userGap")
    public ResponseEntity<GapEditTimeResponse> getUserGap(
            @RequestParam("email") String userEmail, @RequestParam(required = false) LocalDate date) {
        try {
            LocalDate currentDate = (date != null) ? date : LocalDate.now();

            GapEditTimeResponse response = gapTrackService.getUserGapData(userEmail, currentDate);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



}
