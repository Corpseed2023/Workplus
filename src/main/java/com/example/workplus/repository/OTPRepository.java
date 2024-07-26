package com.example.workplus.repository;

import com.example.workplus.model.OTP;
import com.example.workplus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {

    List<OTP> findByUserId(Long userId);

    @Query("select otp from OTP otp where otp.user = :user")
    OTP findByUser(@Param("user") User user);

}