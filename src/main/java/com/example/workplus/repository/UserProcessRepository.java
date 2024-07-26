package com.example.workplus.repository;

import com.example.workplus.model.User;
import com.example.workplus.model.UserProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserProcessRepository extends JpaRepository<UserProcess,Long> {


    List<UserProcess> findByUserEmail(String userEmail);

    List<UserProcess> findByUserEmailAndDate(String userEmail, LocalDate date);

    UserProcess findByUserAndDateAndProcessName(User user, LocalDate date, String processName);
}
