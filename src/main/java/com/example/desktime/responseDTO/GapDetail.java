package com.example.desktime.responseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GapDetail {

    private LocalDateTime lastOfflineTime;
    private LocalDateTime lastOnlineTime;
    private String gapTime;

    public GapDetail(LocalDateTime lastOfflineTime, LocalDateTime lastOnlineTime, String gapTime) {
        this.lastOfflineTime = lastOfflineTime;
        this.lastOnlineTime = lastOnlineTime;
        this.gapTime = gapTime;
    }
}