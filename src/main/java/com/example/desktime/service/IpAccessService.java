package com.example.desktime.service;

import com.example.desktime.model.IPAccess;
import org.bouncycastle.util.IPAddress;

import java.util.List;

public interface IpAccessService {


    String addIpAddress(Long userId, String ipAddress);

    String getCheckIp(String ipAddress);

    List<IPAccess> getIps(Long userId);


}
