package com.example.desktime.responseDTO;

import com.example.desktime.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;
import com.example.desktime.model.Roles;


@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    private String username;
    private String email;

    public UserResponse(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
