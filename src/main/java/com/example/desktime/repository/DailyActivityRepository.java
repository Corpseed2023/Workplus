package com.example.desktime.repository;

import com.example.desktime.model.DailyActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyActivityRepository  extends JpaRepository<DailyActivity,Long> {
}
