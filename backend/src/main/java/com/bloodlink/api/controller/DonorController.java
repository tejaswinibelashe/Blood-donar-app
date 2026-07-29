package com.bloodlink.api.controller;

import com.bloodlink.api.entity.Donor;
import com.bloodlink.api.service.DonorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
@CrossOrigin(origins = "*")
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @GetMapping
    public List<Donor> getAllDonors() {
        return donorService.getAllDonors();
    }

    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyDonors(@RequestParam String bloodGroup, @RequestParam Double lat, @RequestParam Double lng, @RequestParam(defaultValue = "10") Double radius) {
        return ResponseEntity.ok(donorService.findNearbyDonors(bloodGroup, lat, lng, radius));
    }

    @GetMapping("/group/{bloodGroup}")
    public List<Donor> getDonorsByBloodGroup(@PathVariable String bloodGroup) {
        return donorService.getDonorsByBloodGroup(bloodGroup);
    }
}
