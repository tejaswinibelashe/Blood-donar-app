package com.bloodlink.api.controller;

import com.bloodlink.api.entity.BloodRequest;
import com.bloodlink.api.repository.BloodRequestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class BloodRequestController {

    private final BloodRequestRepository requestRepository;

    public BloodRequestController(BloodRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @GetMapping
    public List<BloodRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody BloodRequest request) {
        request.setStatus("PENDING");
        BloodRequest saved = requestRepository.save(request);
        return ResponseEntity.ok(saved);
    }
}
