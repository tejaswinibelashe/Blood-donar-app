package com.bloodlink.api.controller;

import com.bloodlink.api.entity.BloodRequest;
import com.bloodlink.api.service.BloodRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class BloodRequestController {

    private final BloodRequestService requestService;
    private final SimpMessagingTemplate messagingTemplate;

    public BloodRequestController(BloodRequestService requestService, SimpMessagingTemplate messagingTemplate) {
        this.requestService = requestService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping
    public ResponseEntity<List<BloodRequest>> getRequests(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String patientId) {
        
        List<BloodRequest> requests;
        if (patientId != null) {
            requests = requestService.getRequestsByPatientId(patientId);
        } else if (status != null) {
            requests = requestService.getRequestsByStatus(status);
        } else {
            requests = requestService.getAllRequests();
        }
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BloodRequest> getRequestById(@PathVariable String id) {
        return requestService.getRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BloodRequest> createRequest(@RequestBody BloodRequest request) {
        BloodRequest saved = requestService.createRequest(request);
        messagingTemplate.convertAndSend("/topic/requests", saved);
        return ResponseEntity.ok(saved);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<BloodRequest> updateRequestStatus(@PathVariable String id, @RequestParam String status) {
        try {
            BloodRequest updated = requestService.updateRequestStatus(id, status);
            messagingTemplate.convertAndSend("/topic/requests", updated);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable String id) {
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }
}
