package com.example.desktime.repository;


import com.example.desktime.model.Screenshot;
import com.example.desktime.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface ScreenshotRepository extends JpaRepository<Screenshot,Long> {
    List<Screenshot> findByUserAndScreenshotTimeBetween(User user, LocalDateTime localDateTime, LocalDateTime localDateTime1);

    Screenshot findByScreenshotNameAndCreatedAt(String originalFilename, Date date);
}
