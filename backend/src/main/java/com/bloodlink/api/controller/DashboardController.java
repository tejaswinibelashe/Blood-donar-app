package com.bloodlink.api.controller;

import com.bloodlink.api.repository.UserRepository;
import com.bloodlink.api.repository.HospitalRepository;
import com.bloodlink.api.repository.DonationHistoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;
    private final DonationHistoryRepository donationHistoryRepository;

    public DashboardController(UserRepository userRepository, HospitalRepository hospitalRepository, DonationHistoryRepository donationHistoryRepository) {
        this.userRepository = userRepository;
        this.hospitalRepository = hospitalRepository;
        this.donationHistoryRepository = donationHistoryRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        
        long totalDonors = userRepository.countByRole("DONOR");
        long totalPatients = userRepository.countByRole("PATIENT");
        long totalHospitals = hospitalRepository.count();
        long totalDonations = donationHistoryRepository.count();
        
        stats.put("donors", totalDonors);
        stats.put("patients", totalPatients);
        stats.put("hospitals", totalHospitals);
        stats.put("donations", totalDonations);
        
        return ResponseEntity.ok(stats);
    }
}
