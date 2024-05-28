package com.example.desktime.controller;

import com.example.desktime.model.IPAccess;
import com.example.desktime.responseDTO.IPAccessDTO;
import com.example.desktime.service.IpAccessService;
import org.bouncycastle.util.IPAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<IPAccessDTO> checkIp(@RequestParam Long userId) {
        List<IPAccess> fetchedIpAddress = ipAccessService.getIps(userId);

        // Convert IPAccess entities to IPAccessDTOs
        List<IPAccessDTO> ipAccessDTOs = fetchedIpAddress.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ipAccessDTOs;
    }

    private IPAccessDTO convertToDto(IPAccess ipAccess) {
        IPAccessDTO ipdto = new IPAccessDTO();
        ipdto.setId(ipAccess.getId());
        ipdto.setNetworkIpAddress(ipAccess.getNetworkIpAddress());
        return ipdto;
    }


}
