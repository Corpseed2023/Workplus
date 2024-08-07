package com.example.workplus.controller;

import com.example.workplus.model.Visitor;
import com.example.workplus.requestDTO.VisitorRequest;
import com.example.workplus.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    @PostMapping("/visitor/saveVisitor")
    public ResponseEntity<?> saveVisitor(@RequestBody VisitorRequest visitorRequest, @RequestParam String mailId) {
        try {
             visitorService.saveVisitor(visitorRequest,mailId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving GapTrack: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/visitor/getAllVisitors")
    public ResponseEntity<?> getAllVisitors(
            @RequestParam String mailId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Visitor> visitors = visitorService.getAllVisitors(mailId, pageable);
            return new ResponseEntity<>(visitors, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching visitors: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/visitor/editVisitor/{id}")
    public ResponseEntity<?> editVisitor(
            @PathVariable Long id,
            @RequestBody VisitorRequest visitorRequest,
            @RequestParam String mailId) {
        try {
            visitorService.editVisitor(id, visitorRequest, mailId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error editing visitor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
