package com.example.workplus.serviceimpl;

import com.example.workplus.model.Roles;
import com.example.workplus.repository.RolesRepository;
import com.example.workplus.responseDTO.RolesResponse;
import com.example.workplus.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RolesImplService  implements RolesService {

    @Autowired
    private RolesRepository rolesRepository;
    @Override
    public void saveRole(Roles roles) {
        if (roles == null) {
            throw new IllegalArgumentException("Roles object cannot be null");
        }

        // Check if a role with the same name already exists
        Roles existingRole = rolesRepository.findByRoleName(roles.getRoleName());
        if (existingRole != null) {
            throw new IllegalArgumentException("Role with the same name already exists");
        }

        try {
            rolesRepository.save(roles);
        } catch (Exception e) {
            throw new RuntimeException("Error saving role", e);
        }
    }


    @Override
    public List<RolesResponse> getRoles() {

        List<Roles> rolesList = this.rolesRepository.findAll();

        List<RolesResponse> rolesResponseArrayList = new ArrayList<>();

        for (Roles roles: rolesList)
        {
            RolesResponse rolesResponse = new RolesResponse();

            rolesResponse.setId(roles.getId());
            rolesResponse.setRoleName(roles.getRoleName());

            rolesResponseArrayList.add(rolesResponse);

        }
        return rolesResponseArrayList;
    }


}
