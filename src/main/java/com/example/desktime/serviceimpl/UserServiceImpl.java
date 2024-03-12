package com.example.desktime.serviceimpl;

import com.example.desktime.model.User;
import com.example.desktime.repository.UserRepository;
import com.example.desktime.requestDTO.UserRequest;
import com.example.desktime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUserData(UserRequest userRequest) {

        User userData = new User();

        userData.setUsername(userRequest.getUsername());

        userData.setUsername(userRequest.getUsername());
        userData.setPassword(userRequest.getPassword());
        userData.setCreatedBy(userRequest.getCreatedBy());
        userData.setCreatedAt(new Date());
        userData.setUpdatedAt(new Date());
        userData.setEnable(userRequest.isEnable());
        userData.setEmail(userRequest.getEmail());


        userRepository.save(userData);
    }

    @Override
    public ResponseEntity<User> getUserdetails(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            return ResponseEntity.ok(existingUser.get());
        } else {
            // User not found
            return ResponseEntity.notFound().build();
        }
    }
}

