package com.example.desktime.repository;

import com.example.desktime.model.DailyActivity;
import com.example.desktime.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyActivityRepository  extends JpaRepository<DailyActivity,Long> {

    @Modifying
    @Transactional
    @Query(value = "update daily_activity da JOIN user u On da.user_id = u.id set da.logout_time= :logoutTime where u.email = :email  and da.date = :date",nativeQuery = true)
    void updateLogoutTime(@Param("email") String email, @Param("date") LocalDate date, @Param("logoutTime") LocalDateTime logoutTime);

    @Query(value = "SELECT * FROM daily_activity da WHERE da.user_id = (SELECT id FROM user WHERE email = :email) AND da.date = :date LIMIT 1", nativeQuery = true)
    DailyActivity findByUserEmailAndDate(@Param("email") String email, @Param("date") LocalDate date);

    @Query(value = "SELECT da FROM DailyActivity da WHERE da.user = :user AND da.date = :date")
    Optional<DailyActivity> findByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);

    List<DailyActivity> findByUserEmailAndDateBetween(String email, LocalDate startDate, LocalDate endDate);

    @Query(value = "SELECT * FROM daily_activity WHERE user_id = :userId AND date = :date LIMIT 1", nativeQuery = true)
    List<DailyActivity> findByUserIdAndDate(Long userId, LocalDate date);


    @Query(value = "SELECT da.id, da.date, da.login_time, da.logout_time, da.present, da.day_of_week, da.attendance_type, u.username, u.email " +
            "FROM daily_activity da " +
            "JOIN user u ON da.user_id = u.id " +
            "WHERE da.date BETWEEN :startDate AND :endDate " +
            "ORDER BY u.username", nativeQuery = true)
    List<Object[]> findAllUserMonthlyReport(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query(value = "SELECT * FROM workplus.daily_activity a WHERE date = CURRENT_DATE() AND " +
            "user_id IN (SELECT user_id FROM workplus.daily_activity" +
            " WHERE date = CURRENT_DATE() GROUP BY user_id HAVING COUNT(*) > 1)",nativeQuery = true)
    List<DailyActivity> findDuplicateEntryInActivity();

}
