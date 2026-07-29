package com.bloodlink.api.service;

import com.bloodlink.api.entity.Patient;
import com.bloodlink.api.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(String id) {
        return patientRepository.findById(id);
    }
    
    public Optional<Patient> getPatientByUserId(String userId) {
        return patientRepository.findByUserId(userId);
    }

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(String id, Patient updatedPatient) {
        return patientRepository.findById(id).map(patient -> {
            patient.setHospitalName(updatedPatient.getHospitalName());
            patient.setEmergencyLevel(updatedPatient.getEmergencyLevel());
            return patientRepository.save(patient);
        }).orElseThrow(() -> new RuntimeException("Patient not found with id " + id));
    }

    public void deletePatient(String id) {
        patientRepository.deleteById(id);
    }
}
