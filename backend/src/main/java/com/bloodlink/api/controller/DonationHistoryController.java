package com.bloodlink.api.controller;

import com.bloodlink.api.entity.DonationHistory;
import com.bloodlink.api.service.DonationHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "*")
public class DonationHistoryController {

    private final DonationHistoryService historyService;

    public DonationHistoryController(DonationHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public ResponseEntity<List<DonationHistory>> getAllHistory() {
        return ResponseEntity.ok(historyService.getAllHistory());
    }

    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<DonationHistory>> getHistoryByDonorId(@PathVariable String donorId) {
        return ResponseEntity.ok(historyService.getHistoryByDonorId(donorId));
    }

    @PostMapping
    public ResponseEntity<DonationHistory> addHistory(@RequestBody DonationHistory history) {
        return ResponseEntity.ok(historyService.addHistory(history));
    }
}
