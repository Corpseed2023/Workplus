package com.example.desktime.repository;

import com.example.desktime.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    List<OTP> findByUserId(Long userId);
}