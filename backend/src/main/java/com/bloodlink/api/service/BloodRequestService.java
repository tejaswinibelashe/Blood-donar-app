package com.bloodlink.api.service;

import com.bloodlink.api.entity.BloodRequest;
import com.bloodlink.api.repository.BloodRequestRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BloodRequestService {

    private final BloodRequestRepository requestRepository;

    public BloodRequestService(BloodRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public List<BloodRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    public List<BloodRequest> getRequestsByStatus(String status) {
        return requestRepository.findByStatus(status);
    }
    
    public List<BloodRequest> getRequestsByPatientId(String patientId) {
        return requestRepository.findByPatientId(patientId);
    }

    public Optional<BloodRequest> getRequestById(String id) {
        return requestRepository.findById(id);
    }

    public BloodRequest createRequest(BloodRequest request) {
        request.setCreatedAt(new Date());
        if (request.getStatus() == null) {
            request.setStatus("PENDING");
        }
        return requestRepository.save(request);
    }

    public BloodRequest updateRequestStatus(String id, String status) {
        return requestRepository.findById(id).map(request -> {
            request.setStatus(status);
            return requestRepository.save(request);
        }).orElseThrow(() -> new RuntimeException("Blood request not found with id " + id));
    }
    
    public void deleteRequest(String id) {
        requestRepository.deleteById(id);
    }
}
