package com.example.desktime.controller;


import com.example.desktime.requestDTO.GapTrackRequest;
import com.example.desktime.responseDTO.GapTrackResponse;
import com.example.desktime.responseDTO.GapTrackSaveResponse;
import com.example.desktime.responseDTO.GapUserResponse;
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
            GapTrackSaveResponse response = gapTrackService.saveGapTrack(gapTrackRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving GapTrack: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/getUserData")
//    public ResponseEntity<?> userGap

//    @PutMapping("/updateGap")
//    public ResponseEntity<?> updateGapTrack(@RequestBody GapTrackRequest gapTrackRequest) {
//        if (gapTrackRequest == null) {
//            return new ResponseEntity<>("Data Not Found", HttpStatus.NOT_FOUND);
//        }
//
//        try {
//            GapTrackUpdateResponse updateResponse = gapTrackService.updateGapTrack(
//                    gapTrackRequest.getStatus(),
//                    gapTrackRequest.getUserEmail(),
//                    gapTrackRequest.getDate()
//            );
//            return new ResponseEntity<>(updateResponse, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Error saving GapTrack: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//
//
@GetMapping("/getUserGapData")
public ResponseEntity<?> getUserGapData(@RequestParam String userMailId, @RequestParam(required = false) LocalDate date) {
    try {
        LocalDate currentDate = (date != null) ? date : LocalDate.now();
        List<GapTrackResponse> userGapData = gapTrackService.getUserGapData(userMailId, currentDate);

        if (!userGapData.isEmpty()) {
            return new ResponseEntity<>(userGapData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Data not Found", HttpStatus.NOT_FOUND);
        }
    } catch (Exception e) {
        return new ResponseEntity<>("Error fetching user gap data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    @PutMapping("/editReason")
    public void updateReason(@RequestParam String userEmail, @RequestParam Long gapId, @RequestBody String gapReason) {
        try {
            if (gapId != null && gapReason != null && !gapReason.trim().isEmpty()) {
                gapTrackService.updateUserGapReason(userEmail, gapId, gapReason);
            }
        } catch (Exception e) {
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



}

