package com.example.workplus.service;

import com.example.workplus.model.Visitor;
import com.example.workplus.requestDTO.VisitorRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VisitorService {

    void saveVisitor(VisitorRequest visitorRequest, String mailId);

    Page<Visitor> getAllVisitors(String mailId, Pageable pageable);

    void editVisitor(Long id, VisitorRequest visitorRequest, String mailId);
}
