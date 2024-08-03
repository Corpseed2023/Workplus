package com.example.workplus.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatedResponse {

    private Long id;
    private String username;
    private String email;
    private String roles;
    private Date createdAt;

}
