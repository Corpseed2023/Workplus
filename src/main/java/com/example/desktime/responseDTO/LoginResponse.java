package com.example.desktime.responseDTO;

import com.example.desktime.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String message;
    private User user;


    public LoginResponse(String message, User user) {
        this.message = message;
        this.user = user;
    }
}