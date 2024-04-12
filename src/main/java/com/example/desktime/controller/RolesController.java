package com.example.desktime.controller;

import com.example.desktime.model.Roles;
import com.example.desktime.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    private RolesService rolesService;

    @PostMapping("/createRole")
    public ResponseEntity<String> createRole(@RequestBody Roles roles) {
        try {
            rolesService.saveRole(roles);
            return new ResponseEntity<>("Role created successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating the role", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
