package com.example.desktime.service;


import com.example.desktime.model.User;
import com.example.desktime.requestDTO.UserRequest;
import com.example.desktime.requestDTO.UserUpdateRequest;
import com.example.desktime.responseDTO.UserResponse;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {


    ResponseEntity<User> getUserdetails(User user);
    User getUserByUsernameAndEmail(String username, String email);

    void saveUserData(UserRequest userRequest) throws AccessDeniedException;

    List<UserResponse> getAllUsers();

    User authenticateUser(String email, String password);

    boolean existofUserDetails(String username, String email);

    void editUserDetails(Long userId, UserUpdateRequest userUpdateRequest) throws AccessDeniedException;

    void softDeleteUser(Long userId);
}
