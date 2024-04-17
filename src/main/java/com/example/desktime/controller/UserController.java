package com.example.desktime.controller;


import com.example.desktime.model.User;
import com.example.desktime.requestDTO.LoginRequest;
import com.example.desktime.requestDTO.UserRequest;
import com.example.desktime.responseDTO.LoginResponse;
import com.example.desktime.responseDTO.UserResponse;
import com.example.desktime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
//@RequestMapping("/userRequest")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<String> postData(@RequestBody UserRequest userRequest) {
        try {
            userService.saveUserData(userRequest);
            return new ResponseEntity<>("User created successfully!", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid user data: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("One or more roles not found", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/userDetails")
    public ResponseEntity<User> getUserDetails(@RequestParam String username) {
        User user = new User();
        user.setUsername(username);
//        user.setEmail(email);

        ResponseEntity<User> responseEntity = userService.getUserdetails(user);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        } else {

            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/allUsersList")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        try {
            List<UserResponse> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Validate input fields
            if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                return ResponseEntity.badRequest().body(new LoginResponse("Email and password are required", null));
            }

            // Retrieve user by email
            User authenticatedUser = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

            if (authenticatedUser != null) {
                // Generate JWT token or session management logic here
                return ResponseEntity.ok(new LoginResponse("Login successful", authenticatedUser));
            } else {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid email or password", null));
            }
        } catch (Exception e) {
            // Log exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponse("Error processing the request", null));
        }
    }

    @GetMapping("/checkUserExists")
    public ResponseEntity<Boolean> checkUserExists(@RequestParam(required = false) String username,
                                                   @RequestParam(required = false) String email) {
        if (username == null && email == null) {
            return ResponseEntity.badRequest().body(false); // Return false if both parameters are null
        }

        boolean userExists = false;

        if (username != null && email != null) {
            userExists = userService.existofUserDetails(username, email);
        }

        if (userExists) {
            return ResponseEntity.ok(true); // Both username and email exist
        } else {
            return ResponseEntity.notFound().build(); // Either username or email does not exist
        }
    }


    @PutMapping("/editUser")
    public ResponseEntity<String> editUserDetails(@RequestParam Long userId, @RequestBody UserRequest userRequest) {
        try {
            userService.editUserDetails(userId, userRequest);
            return new ResponseEntity<>("User details updated successfully!", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid user data: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("One or more roles not found", HttpStatus.BAD_REQUEST);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>("Only administrators can edit user details", HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
