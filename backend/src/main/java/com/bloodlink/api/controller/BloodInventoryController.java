package com.bloodlink.api.controller;

import com.bloodlink.api.entity.BloodInventory;
import com.bloodlink.api.service.BloodInventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/blood-inventory")
@CrossOrigin(origins = "*")
public class BloodInventoryController {

    private final BloodInventoryService inventoryService;
    private final SimpMessagingTemplate messagingTemplate;

    public BloodInventoryController(BloodInventoryService inventoryService, SimpMessagingTemplate messagingTemplate) {
        this.inventoryService = inventoryService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping
    public ResponseEntity<List<BloodInventory>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/{bloodGroup}")
    public ResponseEntity<BloodInventory> getInventoryByBloodGroup(@PathVariable String bloodGroup) {
        return inventoryService.getInventoryByBloodGroup(bloodGroup)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<BloodInventory> addInventory(@RequestBody BloodInventory request) {
        BloodInventory updated = inventoryService.addOrUpdateInventory(request);
        messagingTemplate.convertAndSend("/topic/inventory", updated);
        return ResponseEntity.ok(updated);
    }
    
    @PostMapping("/consume")
    public ResponseEntity<?> consumeInventory(@RequestParam String bloodGroup, @RequestParam Integer units) {
        try {
            BloodInventory updated = inventoryService.consumeInventory(bloodGroup, units);
            messagingTemplate.convertAndSend("/topic/inventory", updated);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
