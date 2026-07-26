package com.bloodlink.api.service;

import com.bloodlink.api.dto.UserDTO;
import com.bloodlink.api.entity.Donor;
import com.bloodlink.api.mapper.UserMapper;
import com.bloodlink.api.repository.DonorRepository;
import com.bloodlink.api.util.GpsUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DonorService {

    private final DonorRepository donorRepository;
    private final UserMapper userMapper;

    public DonorService(DonorRepository donorRepository, UserMapper userMapper) {
        this.donorRepository = donorRepository;
        this.userMapper = userMapper;
    }
    
    // In a full implementation, you'd autowire a LocationRepository to fetch donor locations

    /**
     * Finds nearby donors based on Blood Group and a specified radius in kilometers.
     */
    public List<UserDTO> findNearbyDonors(String bloodGroup, double searchLat, double searchLng, double radiusKm) {
        List<Donor> matchingDonors = donorRepository.findByBloodGroupAndIsAvailableTrue(bloodGroup);
        List<UserDTO> nearbyDonors = new ArrayList<>();

        for (Donor donor : matchingDonors) {
            // Placeholder: Assume we fetched the donor's location from DB
            // Location loc = locationRepository.findByUser(donor);
            double donorLat = searchLat + 0.05; // mock for demo
            double donorLng = searchLng + 0.05; // mock for demo

            double distance = GpsUtils.calculateDistance(searchLat, searchLng, donorLat, donorLng);
            
            if (distance <= radiusKm) {
                UserDTO dto = userMapper.toDto(donor);
                // In a true DTO we might add a 'distance' field specifically for this response
                nearbyDonors.add(dto);
            }
        }
        
        // Sort by closest distance (currently omitted as DTO lacks distance field for this mock)
        return nearbyDonors;
    }
}
