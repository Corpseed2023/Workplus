package com.example.workplus.repository;

import com.example.workplus.model.GapTrack;
import com.example.workplus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GapRepository  extends JpaRepository<GapTrack, Long> {

        @Query("SELECT g FROM GapTrack g WHERE g.user = :user")
        List<GapTrack> fetchAllUserGapData(@Param("user") User user);

        @Query("SELECT g FROM GapTrack g WHERE g.user = :user AND g.date = :date ORDER BY g.gapStartTime")
        List<GapTrack> fetchUserGapData(@Param("user") User user, @Param("date") LocalDate date);

        @Query("SELECT g FROM GapTrack g WHERE g.user = :user AND g.id BETWEEN :lastOfflineId AND :lastOnlineId")
        List<GapTrack> findByGapId(Long lastOfflineId, Long lastOnlineId, User user);

//        @Query("SELECT g FROM GapTrack g WHERE g.user = :user AND g.id BETWEEN :lastOfflineId AND :lastOnlineId")
//        List<GapTrack> findByStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime, User user);


}
