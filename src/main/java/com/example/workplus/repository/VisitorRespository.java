package com.example.workplus.repository;

import com.example.workplus.model.Visitor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorRespository extends JpaRepository<Visitor,Long> {

    Page<Visitor> findAll(Pageable pageable);

}
