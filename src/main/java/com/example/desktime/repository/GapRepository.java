package com.example.desktime.repository;

import com.example.desktime.model.GapTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GapRepository  extends JpaRepository<GapTrack, Long> {
}
