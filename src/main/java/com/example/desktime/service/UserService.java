package com.example.desktime.service;


import com.example.desktime.model.User;
import com.example.desktime.requestDTO.UserRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    void saveUserData(UserRequest userRequest);

    ResponseEntity<User> getUserdetails(User user);

    User getUserByUsernameAndEmail(String username, String email);
}
