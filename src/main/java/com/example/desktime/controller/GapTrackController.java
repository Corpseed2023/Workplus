package com.example.desktime.controller;


import com.example.desktime.model.GapTrack;
import com.example.desktime.requestDTO.GapTrackRequest;
import com.example.desktime.service.GapTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gap-track")
@CrossOrigin(origins = "*", maxAge = 3600)

public class GapTrackController {

    @Autowired
    private GapTrackService gapTrackService;

    @PostMapping("/save")
    public ResponseEntity<?> saveGapTrack(@RequestBody GapTrackRequest gapTrackRequest) {
        try {
            GapTrack savedGapTrack = gapTrackService.saveGapTrack(gapTrackRequest);
            return new ResponseEntity<>(savedGapTrack, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving GapTrack: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

