package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.repository.MedicalRecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordsService {

    private final MedicalRecordsRepository medicalRecordsRepository;

    @Autowired
    public MedicalRecordsService(MedicalRecordsRepository medicalRecordsRepository) {
        this.medicalRecordsRepository = medicalRecordsRepository;
    }

    public void createMedicalsRecords(String firstName, String lastName, String birthdate, List<String> medications, List<String>allergies){
        MedicalRecords medicalRecords = new MedicalRecords();
        medicalRecords.setFirstName(firstName);
        medicalRecords.setLastName(lastName);
        medicalRecords.setBirthdate(birthdate);
        medicalRecords.setMedications(medications);
        medicalRecords.setAllergies(allergies);
        medicalRecordsRepository.addMedicalRecords(medicalRecords);
    }

    public MedicalRecords getMedicalRecordsByName(String firstName, String lastName) {
        List<MedicalRecords> medicalRecordsList = medicalRecordsRepository.getAllMedicalRecords();
        for (MedicalRecords medicalRecords : medicalRecordsList) {
            if (medicalRecords.getFirstName().equals(firstName) && medicalRecords.getLastName().equals(lastName)) {
                return (medicalRecords);
            }
        }
        return null;
    }
}
