package com.example.workplus.service;

import com.example.workplus.model.IPAccess;

import java.util.List;

public interface IpAccessService {


    String addIpAddress(Long userId, String ipAddress);

    String getCheckIp(String ipAddress);

    List<IPAccess> getIps(Long userId);


}
