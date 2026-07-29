package com.bloodlink.api.repository;

import com.bloodlink.api.entity.BloodInventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BloodInventoryRepository extends MongoRepository<BloodInventory, String> {
    Optional<BloodInventory> findByBloodGroup(String bloodGroup);
}
