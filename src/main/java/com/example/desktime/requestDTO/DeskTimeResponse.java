package com.example.desktime.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeskTimeResponse {
    private Long id;
    private String screenshot;
    private String process;
    private String timeConvention;
    private String message;
}