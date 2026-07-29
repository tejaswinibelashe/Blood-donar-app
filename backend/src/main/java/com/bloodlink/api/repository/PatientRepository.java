package com.bloodlink.api.repository;

import com.bloodlink.api.entity.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    java.util.Optional<Patient> findByUserId(String userId);
}
