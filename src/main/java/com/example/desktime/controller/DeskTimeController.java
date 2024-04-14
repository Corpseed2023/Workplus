package com.example.desktime.controller;


import com.example.desktime.model.Desktime;
import com.example.desktime.requestDTO.DeskTimeRequest;
import com.example.desktime.service.DesktimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeskTimeController {

    @Autowired
    private DesktimeService desktimeService;

    @PostMapping("/saveDeskTime")
    public ResponseEntity<String> saveDesktime(@RequestBody DeskTimeRequest deskTimeRequest,
                                               @RequestParam String username,
                                               @RequestParam String email) {
        try {
            desktimeService.saveDesktimeData(username, email, deskTimeRequest);
            return new ResponseEntity<>("Desktime data saved successfully!", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
