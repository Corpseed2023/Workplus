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

    //complete the method here and also put limit so it can fetch single data for this mail id and current date

//    @Query(value = "Select * from daily_activity da where da.user = : (select id from user where email =: email) and da.date= : date limit 1" ,nativeQuery = true)
//    DailyActivity findByUserEmailAndDate(@Param("email") String email, @Param("date") LocalDate date);

    @Query(value = "SELECT * FROM daily_activity da WHERE da.user_id = (SELECT id FROM user WHERE email = :email) AND da.date = :date LIMIT 1", nativeQuery = true)
    DailyActivity findByUserEmailAndDate(@Param("email") String email, @Param("date") LocalDate date);


    @Query(value = "SELECT da FROM DailyActivity da WHERE da.user = :user AND da.date = :date")
    Optional<DailyActivity> findByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);

    List<DailyActivity> findByUserEmailAndDateBetween(String email, LocalDate startDate, LocalDate endDate);

    @Query(value = "SELECT * FROM daily_activity WHERE user_id = :userId AND date = :date", nativeQuery = true)
    Optional<DailyActivity> findByUserIdAndDate(Long userId, LocalDate date);

    @Query(value = "SELECT da.id, da.date, da.login_time, da.logout_time, da.present, u.username, u.email " +
            "FROM daily_activity da " +
            "JOIN user u ON da.user_id = u.id " +
            "WHERE da.date BETWEEN :startDate AND :endDate " +
            "ORDER BY u.id", nativeQuery = true)
    List<Object[]> findAllUserMonthlyReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


}
