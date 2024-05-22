package com.example.desktime.repository;

import com.example.desktime.model.GapTrack;
import com.example.desktime.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface GapRepository  extends JpaRepository<GapTrack, Long> {


        @Query("SELECT g FROM GapTrack g WHERE g.user= :user AND g.date = :date and g.availability=false" )
        GapTrack findLastAvailability(@Param("user") User user, @Param("date") LocalDate date);


}
