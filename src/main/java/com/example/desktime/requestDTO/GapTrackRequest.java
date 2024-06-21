package com.example.desktime.requestDTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GapTrackRequest {

    private String status;

    private String userEmail;

}
