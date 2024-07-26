package com.example.workplus.controller;


import com.example.workplus.requestDTO.UserProcessRequest;
import com.example.workplus.responseDTO.UserProcessResponse;
import com.example.workplus.service.UserProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api")

public class UserProcessController {

    @Autowired
    private UserProcessService userProcessService;

    @PostMapping("/saveUserProcess")
    public ResponseEntity<?> saveUserProcess(@RequestBody UserProcessRequest userProcessRequest) {
        try {
            UserProcessResponse userProcessResponse = userProcessService.saveUserProcess(userProcessRequest);
            return new ResponseEntity<>(userProcessResponse, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format. Please provide the date in yyyy-MM-dd format.", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getUserProcesses")
    public ResponseEntity<?> getUserProcesses(@RequestParam String userEmail) {
        try {
            List<UserProcessResponse> userProcesses = userProcessService.getUserProcessesByEmail(userEmail);
            return new ResponseEntity<>(userProcesses, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUserDateProcess")
    public List<UserProcessResponse> getUserDateProcess(@RequestParam String userEmail,  @RequestParam(required = false) LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }
        return userProcessService.getUserProcessWithEmailAndDate(userEmail, date);
    }


}
