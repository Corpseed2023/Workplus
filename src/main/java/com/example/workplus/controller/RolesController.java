package com.example.workplus.controller;

import com.example.workplus.model.Roles;
import com.example.workplus.responseDTO.RolesResponse;
import com.example.workplus.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*", maxAge = 3600)
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


    @GetMapping("/getRoles")
    public ResponseEntity<List<RolesResponse>> getRoles() {

        List<RolesResponse> rolesResponsesList= rolesService.getRoles();
        try {
            if (rolesResponsesList!=null)
            {
                return ResponseEntity.ok(rolesResponsesList);

            }
            else
                return ResponseEntity.noContent().build();
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }


    }







}
