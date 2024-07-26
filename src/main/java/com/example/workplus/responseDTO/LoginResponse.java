package com.example.workplus.responseDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class LoginResponse {
    private String message;
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
    private Date createdAt;
    private Date updatedAt;



    public LoginResponse(String message) {
        this.message = message;
    }


}