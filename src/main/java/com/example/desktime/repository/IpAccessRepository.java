package com.example.desktime.repository;

import com.example.desktime.model.IPAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IpAccessRepository extends JpaRepository<IPAccess,Long> {

//    @Query(value = "select i from IPAccess where i.network_ip_address=:ipAddress")
//    String findByIpAddress(@Param(ipAddress) String ipAddress);

    @Query("SELECT ip FROM IPAccess ip WHERE ip.networkIpAddress = :ipAddress")
    IPAccess findByNetworkIpAddress(@Param("ipAddress") String ipAddress);
}
