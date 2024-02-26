package com.example.desktime.controller;

import com.example.desktime.model.SystemActivity;
import com.example.desktime.service.SystemActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system-activity")
public class SystemActivityController {

    @Autowired
    private SystemActivityService systemActivityService;

    @PostMapping("/log-activity")
    public ResponseEntity<String> logSystemActivity(@RequestBody SystemActivity systemActivity) {
        try {
            systemActivityService.logSystemActivity(systemActivity);
            return new ResponseEntity<>("System activity logged successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error logging system activity", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
