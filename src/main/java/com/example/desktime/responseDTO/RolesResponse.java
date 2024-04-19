package com.example.desktime.responseDTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class RolesResponse {

    private Long  id;
    private String roleName;
}
