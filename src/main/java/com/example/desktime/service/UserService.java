package com.example.desktime.service;


import com.example.desktime.model.User;
import com.example.desktime.requestDTO.ResetPasswordRequest;
import com.example.desktime.requestDTO.UserRequest;
import com.example.desktime.requestDTO.UserUpdateRequest;
import com.example.desktime.responseDTO.SingleUserResponse;
import com.example.desktime.responseDTO.UserResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {


    User getUserByUsernameAndEmail(String username, String email);

    void saveUserData(UserRequest userRequest) throws AccessDeniedException, MessagingException;

    List<UserResponse> getAllUsers();

    User authenticateUser(String email, String password);

    boolean existofUserDetails(String username, String email);


    void softDeleteUser(Long userId);

    ResponseEntity<SingleUserResponse> getSingleUserDetails(String usernameMail);


    void initiatePasswordReset(String email, String password) throws MessagingException;

    UserResponse editUserDetails(Long userId, UserUpdateRequest userUpdateRequest) throws AccessDeniedException ;
}
