package com.example.desktime.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class CommonUtil {

    private static final ZoneId INDIA_ZONE_ID = ZoneId.of("Asia/Kolkata");

    /**
     * Returns the current date and time according to the Indian time zone.
     *
     * @return the current date and time as a Date object
     */
    public static Date getCurrentTimeInIndia() {
        return Date.from(LocalDateTime.now().atZone(INDIA_ZONE_ID).toInstant());
    }


}
