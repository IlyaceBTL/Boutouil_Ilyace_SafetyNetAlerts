package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.repository.MedicalRecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class that provides operations for managing medical records.
 */
@Service
public class MedicalRecordsService {

    private final MedicalRecordsRepository medicalRecordsRepository;

    @Autowired
    public MedicalRecordsService(MedicalRecordsRepository medicalRecordsRepository) {
        this.medicalRecordsRepository = medicalRecordsRepository;
    }

    /**
     * Creates and stores a new medical record.
     *
     * @param medicalRecords The medical record to create.
     * @return The created medical record.
     */
    public MedicalRecords createMedicalRecords(MedicalRecords medicalRecords) {
        medicalRecordsRepository.addMedicalRecords(medicalRecords);
        return medicalRecords;
    }

    /**
     * Retrieves a medical record by the given first and last name.
     *
     * @param firstName The first name of the person.
     * @param lastName  The last name of the person.
     * @return An Optional containing the medical record if found, or empty otherwise.
     */
    public Optional<MedicalRecords> getMedicalRecordsByName(String firstName, String lastName) {
        return medicalRecordsRepository.getMedicalRecords(firstName, lastName);
    }

    /**
     * Updates an existing medical record.
     *
     * @param medicalRecords The medical record with updated data.
     */
    public void updateMedicalRecords(MedicalRecords medicalRecords) {
        medicalRecordsRepository.updateMedicalRecords(medicalRecords);
    }

    /**
     * Deletes a medical record by first and last name.
     *
     * @param firstName The first name of the person.
     * @param lastName  The last name of the person.
     */
    public void deleteMedicalRecords(String firstName, String lastName) {
        medicalRecordsRepository.deleteMedicalRecords(firstName, lastName);
    }

    /**
     * Returns a blank/default medical record object.
     *
     * @return A default MedicalRecords object with empty or placeholder fields.
     */
    public MedicalRecords blankMedicalRecords() {
        return medicalRecordsRepository.blankMedicalRecords();
    }
}
