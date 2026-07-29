package com.bloodlink.api.repository;

import com.bloodlink.api.entity.DonationHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationHistoryRepository extends MongoRepository<DonationHistory, String> {
    java.util.List<DonationHistory> findByDonorId(String donorId);
}
