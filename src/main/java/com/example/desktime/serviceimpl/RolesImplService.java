package com.example.desktime.serviceimpl;

import com.example.desktime.model.Roles;
import com.example.desktime.repository.RolesRepository;
import com.example.desktime.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
