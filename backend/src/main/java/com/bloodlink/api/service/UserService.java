package com.bloodlink.api.service;

import com.bloodlink.api.entity.User;
import com.bloodlink.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(String role) {
        return userRepository.findByRoleIgnoreCase(role);
    }
    
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
    
    public User updateUser(String id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setFullName(updatedUser.getFullName());
            user.setPhone(updatedUser.getPhone());
            user.setAddress(updatedUser.getAddress());
            user.setCity(updatedUser.getCity());
            user.setState(updatedUser.getState());
            user.setPincode(updatedUser.getPincode());
            user.setBloodGroup(updatedUser.getBloodGroup());
            user.setLatitude(updatedUser.getLatitude());
            user.setLongitude(updatedUser.getLongitude());
            user.setAvailability(updatedUser.getAvailability());
            user.setProfilePictureUrl(updatedUser.getProfilePictureUrl());
            user.setUpdatedAt(new java.util.Date());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }
    
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
