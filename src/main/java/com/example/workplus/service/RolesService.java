package com.example.workplus.service;

import com.example.workplus.model.Roles;
import com.example.workplus.responseDTO.RolesResponse;

import java.util.List;

public interface RolesService {

    void saveRole(Roles roles);

    List<RolesResponse> getRoles();
}