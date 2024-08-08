package com.example.workplus.service;


import com.example.workplus.model.User;
import com.example.workplus.requestDTO.UserRequest;
import com.example.workplus.requestDTO.UserUpdateRequest;
import com.example.workplus.responseDTO.userResponse.SingleUserResponse;
import com.example.workplus.responseDTO.userResponse.UserResponse;
import com.example.workplus.responseDTO.userResponse.UserUpdatedResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {


    User getUserByUsernameAndEmail(String username, String email);

    void saveUserData(UserRequest userRequest) throws AccessDeniedException, MessagingException;


    User authenticateUser(String email, String password);

    boolean existofUserDetails(String username, String email);

    ResponseEntity<SingleUserResponse> getSingleUserDetails(String usernameMail);

    void initiatePasswordReset(String email, String password) throws MessagingException;

    UserUpdatedResponse editUserDetails(Long userId, UserUpdateRequest userUpdateRequest) throws AccessDeniedException ;

    void softDeleteUsers(List<Long> userIds);

    List<UserResponse> getAllUsers(int page, int size);
}
