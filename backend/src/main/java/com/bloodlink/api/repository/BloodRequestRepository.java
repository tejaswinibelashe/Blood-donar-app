package com.bloodlink.api.repository;

import com.bloodlink.api.entity.BloodRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodRequestRepository extends MongoRepository<BloodRequest, String> {
    List<BloodRequest> findByStatus(String status);
    List<BloodRequest> findByPatientId(String patientId);
}
