package com.bloodlink.api.repository;

import com.bloodlink.api.entity.Donor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonorRepository extends MongoRepository<Donor, String> {
    List<Donor> findByBloodGroupAndIsAvailableTrue(String bloodGroup);
}
