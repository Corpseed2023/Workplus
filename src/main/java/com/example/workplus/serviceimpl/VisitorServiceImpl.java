package com.example.workplus.serviceimpl;

import com.example.workplus.model.Roles;
import com.example.workplus.model.User;
import com.example.workplus.model.Visitor;
import com.example.workplus.repository.RolesRepository;
import com.example.workplus.repository.UserRepository;
import com.example.workplus.repository.VisitorRespository;
import com.example.workplus.requestDTO.VisitorRequest;
import com.example.workplus.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class VisitorServiceImpl implements VisitorService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private VisitorRespository visitorRespository;

    @Override
    public void saveVisitor(VisitorRequest visitorRequest, String mailId) {
        // Check if the user exists
        User userData = userRepository.findUserByEmail(mailId);
        if (userData == null) {
            throw new IllegalArgumentException("User not found with email: " + mailId);
        }

        // Check if the user has ADMIN or FRONT_DESK roles
        Set<Roles> roles = userData.getRoles();
        boolean hasRequiredRole = roles.stream()
                .anyMatch(role -> "ADMIN".equals(role.getRoleName()) || "FRONT_DESK".equals(role.getRoleName()));

        if (!hasRequiredRole) {
            throw new IllegalArgumentException("User does not have required roles to save visitor data.");
        }

        // Save visitor data
        Visitor visitor = new Visitor();
        visitor.setName(visitorRequest.getName());
        visitor.setContactNumber(visitorRequest.getContactNumber());
        visitor.setVisitorMailId(visitorRequest.getVisitorMailId());
        visitor.setCompanyName(visitorRequest.getCompanyName());
        visitor.setReferenceThrough(visitorRequest.getReferenceThrough());
        visitor.setPurpose(visitorRequest.getPurpose());
        visitor.setUser(userData);
        visitor.setCreatedBy(userData.getId());
        visitor.setCreatedAt(new Date());
        visitor.setUpdatedAt(new Date());
        visitor.setEnable(true);

        visitorRespository.save(visitor);
    }



    @Override
    public Page<Visitor> getAllVisitors(String mailId, Pageable pageable) {
        // Check if the user exists
        User userData = userRepository.findUserByEmail(mailId);
        if (userData == null) {
            throw new IllegalArgumentException("User not found with email: " + mailId);
        }

        // Check if the user has ADMIN or FRONT_DESK roles
        Set<Roles> roles = userData.getRoles();
        boolean hasRequiredRole = roles.stream()
                .anyMatch(role -> "ADMIN".equals(role.getRoleName()) || "FRONT_DESK".equals(role.getRoleName()));

        if (!hasRequiredRole) {
            throw new IllegalArgumentException("User does not have required roles to fetch visitor data.");
        }

        // Fetch all visitors with pagination
        return visitorRespository.findAll(pageable);
    }

    @Override
    public void editVisitor(Long visitorId, VisitorRequest visitorRequest, String mailId) {
        // Check if the user exists
        User userData = userRepository.findUserByEmail(mailId);
        if (userData == null) {
            throw new IllegalArgumentException("User not found with email: " + mailId);
        }

        // Check if the user has ADMIN or FRONT_DESK roles
        Set<Roles> roles = userData.getRoles();
        boolean hasRequiredRole = roles.stream()
                .anyMatch(role -> "ADMIN".equals(role.getRoleName()) || "FRONT_DESK".equals(role.getRoleName()));

        if (!hasRequiredRole) {
            throw new IllegalArgumentException("User does not have required roles to edit visitor data.");
        }

        // Find existing visitor
        Visitor visitor = visitorRespository.findById(visitorId)
                .orElseThrow(() -> new IllegalArgumentException("Visitor not found with id: " + visitorId));

        // Update visitor data
        visitor.setName(visitorRequest.getName());
        visitor.setContactNumber(visitorRequest.getContactNumber());
        visitor.setVisitorMailId(visitorRequest.getVisitorMailId());
        visitor.setCompanyName(visitorRequest.getCompanyName());
        visitor.setReferenceThrough(visitorRequest.getReferenceThrough());
        visitor.setPurpose(visitorRequest.getPurpose());
        visitor.setUpdatedAt(new Date());

        visitorRespository.save(visitor);
    }






}
