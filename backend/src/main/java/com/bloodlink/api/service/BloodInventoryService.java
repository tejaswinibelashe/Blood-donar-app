package com.bloodlink.api.service;

import com.bloodlink.api.entity.BloodInventory;
import com.bloodlink.api.repository.BloodInventoryRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BloodInventoryService {

    private final BloodInventoryRepository inventoryRepository;

    public BloodInventoryService(BloodInventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<BloodInventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Optional<BloodInventory> getInventoryByBloodGroup(String bloodGroup) {
        return inventoryRepository.findByBloodGroup(bloodGroup);
    }

    public BloodInventory addOrUpdateInventory(BloodInventory inventoryRequest) {
        Optional<BloodInventory> existingOpt = inventoryRepository.findByBloodGroup(inventoryRequest.getBloodGroup());
        
        if (existingOpt.isPresent()) {
            BloodInventory existing = existingOpt.get();
            if (existing.getAvailableUnits() == null) existing.setAvailableUnits(0);
            if (inventoryRequest.getAvailableUnits() == null) inventoryRequest.setAvailableUnits(0);
            existing.setAvailableUnits(existing.getAvailableUnits() + inventoryRequest.getAvailableUnits());
            return inventoryRepository.save(existing);
        } else {
            if (inventoryRequest.getAvailableUnits() == null) inventoryRequest.setAvailableUnits(0);
            return inventoryRepository.save(inventoryRequest);
        }
    }
    
    public BloodInventory consumeInventory(String bloodGroup, Integer units) {
        BloodInventory existing = inventoryRepository.findByBloodGroup(bloodGroup)
                .orElseThrow(() -> new RuntimeException("Blood group not found in inventory: " + bloodGroup));
                
        if (existing.getAvailableUnits() == null || existing.getAvailableUnits() < units) {
            throw new RuntimeException("Not enough units available for " + bloodGroup);
        }
        
        existing.setAvailableUnits(existing.getAvailableUnits() - units);
        return inventoryRepository.save(existing);
    }
}
