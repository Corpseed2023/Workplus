package com.example.desktime.service;

public interface IpAccessService {


    String addIpAddress(Long userId, String ipAddress);

    String getCheckIp(String ipAddress);
}
