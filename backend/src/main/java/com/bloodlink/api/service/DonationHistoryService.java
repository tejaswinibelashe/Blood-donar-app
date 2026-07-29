package com.bloodlink.api.service;

import com.bloodlink.api.entity.DonationHistory;
import com.bloodlink.api.repository.DonationHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DonationHistoryService {

    private final DonationHistoryRepository historyRepository;

    public DonationHistoryService(DonationHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<DonationHistory> getHistoryByDonorId(String donorId) {
        return historyRepository.findByDonorId(donorId);
    }
    
    public List<DonationHistory> getAllHistory() {
        return historyRepository.findAll();
    }

    public DonationHistory addHistory(DonationHistory history) {
        if (history.getDonationDate() == null) {
            history.setDonationDate(new Date());
        }
        return historyRepository.save(history);
    }
}
