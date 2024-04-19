package com.example.desktime.service;

import com.example.desktime.model.Roles;
import com.example.desktime.responseDTO.RolesResponse;

import java.util.List;

public interface RolesService {

    void saveRole(Roles roles);

    List<RolesResponse> getRoles();
}