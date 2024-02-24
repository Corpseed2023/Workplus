package com.example.desktime.controller;


import com.example.desktime.model.User;
import com.example.desktime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/endpoint")
    public ResponseEntity<String> postData(@RequestBody User user) {
        try {
            userService.saveUserData(user);
            return new ResponseEntity<>("Data received successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
