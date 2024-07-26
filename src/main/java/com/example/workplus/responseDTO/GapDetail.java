package com.example.workplus.responseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GapDetail {

    private Long lastOfflineId;
    private LocalDateTime lastOfflineTime;
    private Long lastOnlineId;
    private LocalDateTime lastOnlineTime;
    private String gapTime;
    private String reason;
    private boolean availability;

    public GapDetail(Long lastOfflineId, LocalDateTime lastOfflineTime, Long lastOnlineId, LocalDateTime lastOnlineTime, String gapTime, String reason, boolean availability) {
        this.lastOfflineId = lastOfflineId;
        this.lastOfflineTime = lastOfflineTime;
        this.lastOnlineId = lastOnlineId;
        this.lastOnlineTime = lastOnlineTime;
        this.gapTime = gapTime;
        this.reason = reason;
        this.availability = availability;
    }
}