package com.example.desktime.repository;

import com.example.desktime.model.UserProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProcessRepository extends JpaRepository<UserProcess,Long> {


}
