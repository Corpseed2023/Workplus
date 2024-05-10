package com.example.desktime.requestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {

    private String username;
    private String email;
    private Long createdBy;
    private String password;
    private Date createdAt;
    private Date updatedAt;
    private boolean isEnable;
    private String roleNames;

}
