package com.example.desktime.controller;

import com.example.desktime.service.IpAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class IpController {

    @Autowired
    private IpAccessService ipAccessService;

    @PostMapping("/addIp")
    public ResponseEntity<?> addIp(@RequestParam Long userId, @RequestParam String ipAddress) {


        if (userId == null || ipAddress == null || ipAddress.isEmpty()) {
            return ResponseEntity.badRequest().body("User or Ip Address is null ");
        }

       String savedData= this.ipAccessService.addIpAddress(userId, ipAddress);

        return ResponseEntity.ok("userId: " + userId + ", ipAddress: " + ipAddress);
    }

    @GetMapping("/checkIp")
    public String checkIp(@RequestParam String ipAddress) {

        String fetchedIpAddress = ipAccessService.getCheckIp(ipAddress);

        return fetchedIpAddress;
    }

}
