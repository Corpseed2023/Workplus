package com.example.workplus.repository;



import com.example.workplus.model.Ticket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Ticket t SET t.isEnable = false WHERE t.id = :id")
    int updateIsEnable(@Param("id") Long id);
}
