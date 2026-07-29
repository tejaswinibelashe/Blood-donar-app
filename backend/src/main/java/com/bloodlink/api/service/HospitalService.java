package com.bloodlink.api.service;

import com.bloodlink.api.entity.Hospital;
import com.bloodlink.api.repository.HospitalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    public Optional<Hospital> getHospitalById(String id) {
        return hospitalRepository.findById(id);
    }
    

    public Hospital createHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    public Hospital updateHospital(String id, Hospital updatedHospital) {
        return hospitalRepository.findById(id).map(hospital -> {
            hospital.setHospitalName(updatedHospital.getHospitalName());
            hospital.setAddress(updatedHospital.getAddress());
            hospital.setPhone(updatedHospital.getPhone());
            hospital.setEmail(updatedHospital.getEmail());
            hospital.setLatitude(updatedHospital.getLatitude());
            hospital.setLongitude(updatedHospital.getLongitude());
            hospital.setOpeningHours(updatedHospital.getOpeningHours());
            hospital.setAvailableUnits(updatedHospital.getAvailableUnits());
            return hospitalRepository.save(hospital);
        }).orElseThrow(() -> new RuntimeException("Hospital not found with id " + id));
    }

    public void deleteHospital(String id) {
        hospitalRepository.deleteById(id);
    }
}
