package com.bloodlink.api.repository;

import com.bloodlink.api.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    long countByRole(String role);
    java.util.List<User> findByRoleIgnoreCase(String role);
}
