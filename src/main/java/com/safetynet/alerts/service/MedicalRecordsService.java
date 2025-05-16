package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.repository.MedicalRecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordsService {

    private final MedicalRecordsRepository medicalRecordsRepository;

    @Autowired
    public MedicalRecordsService(MedicalRecordsRepository medicalRecordsRepository) {
        this.medicalRecordsRepository = medicalRecordsRepository;
    }

    public MedicalRecords createMedicalRecords(MedicalRecords medicalRecords) {
        medicalRecordsRepository.addMedicalRecords(medicalRecords);
        return medicalRecords;
    }

    public MedicalRecords getMedicalRecordsByName(String firstName, String lastName) {
        return medicalRecordsRepository.getMedicalRecords(firstName, lastName);
    }

    public MedicalRecords updtadeMedicalRecords(MedicalRecords medicalRecords) {
        medicalRecordsRepository.updateMedicalRecords(medicalRecords);
        return medicalRecords;
    }

    public void deleteMedicalRecords(String firstName, String lastName) {
        medicalRecordsRepository.deleteMedicalRecords(firstName, lastName);
    }
}
