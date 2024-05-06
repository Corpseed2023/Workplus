package com.example.desktime.repository;

import com.example.desktime.model.DailyActivity;
import com.example.desktime.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyActivityRepository  extends JpaRepository<DailyActivity,Long> {


    Optional<DailyActivity> findByUser(User user);

    DailyActivity findByUserEmail(String email);

    DailyActivity findByUserEmailAndDate(String email, LocalDate currentDate);

    Optional<DailyActivity> findByUserAndDate(User user, LocalDate now);

    List<DailyActivity> findByUserEmailAndDateBetween(String email, LocalDate startDate, LocalDate endDate);
}
