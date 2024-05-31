package com.example.desktime.repository;

import com.example.desktime.model.DailyActivity;
import com.example.desktime.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "SELECT * FROM daily_activity WHERE user_id = :userId AND date = :date", nativeQuery = true)
    Optional<DailyActivity> findByUserIdAndDate(Long userId, LocalDate date);

    @Query(value = "SELECT * FROM workpulse.daily_activity WHERE date BETWEEN :startDate AND :endDate ORDER BY user_id", nativeQuery = true)
    List<DailyActivity> findAllUserMonthlyReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
