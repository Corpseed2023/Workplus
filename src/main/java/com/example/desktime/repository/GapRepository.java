package com.example.desktime.repository;

import com.example.desktime.model.GapTrack;
import com.example.desktime.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GapRepository  extends JpaRepository<GapTrack, Long> {

        @Query("SELECT g FROM GapTrack g WHERE g.user = :user AND g.date = :date AND g.availability = false ORDER BY g.gapEndTime DESC")
        List<GapTrack> findLastAvailability(@Param("user") User user, @Param("date") LocalDate date);

//        @Query("select g from GapTrack g where g.user = :user and g.date = :date")
//        List<GapTrack> fetchUserGapData(User user, LocalDate date);

        @Query("select g from GapTrack g where g.user = :user and g.date = :date")
        List<GapTrack> fetchUserGapData(@Param("user") User user, @Param("date") LocalDate date);


        @Query("SELECT g FROM GapTrack g WHERE g.user = :user")
        List<GapTrack> fetchAllUserGapData(@Param("user") User user);
}
