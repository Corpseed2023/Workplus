package com.example.workplus.repository;


import com.example.workplus.model.Screenshot;
import com.example.workplus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScreenshotRepository extends JpaRepository<Screenshot,Long> {
    List<Screenshot> findByUserAndScreenshotTimeBetween(User user, LocalDateTime localDateTime, LocalDateTime localDateTime1);


    @Query("SELECT s FROM screenshot s WHERE s.user = :user AND s.date = :date")
    List<Screenshot> findByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);



}
