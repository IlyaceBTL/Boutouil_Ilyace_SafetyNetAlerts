package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.MedicalRecords;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for managing in-memory medical records.
 */
@Repository
public class MedicalRecordsRepository {

    private static final Logger logger = LogManager.getLogger(MedicalRecordsRepository.class.getName());
    private final List<MedicalRecords> medicalRecordsList = new ArrayList<>();

    /**
     * Retrieves all medical records.
     *
     * @return A list of all stored medical records.
     */
    public List<MedicalRecords> getAllMedicalRecords() {
        return new ArrayList<>(medicalRecordsList);
    }

    /**
     * Adds a new medical record to the repository.
     *
     * @param medicalRecords The medical record to add.
     */
    public void addMedicalRecords(MedicalRecords medicalRecords) {
        medicalRecordsList.add(medicalRecords);
    }

    /**
     * Retrieves a medical record by first and last name.
     *
     * @param firstName The person's first name.
     * @param lastName  The person's last name.
     * @return An Optional containing the medical record if found, or empty otherwise.
     */
    public Optional<MedicalRecords> getMedicalRecords(String firstName, String lastName) {
        return medicalRecordsList.stream()
                .filter(medicalRecordsLooking -> medicalRecordsLooking.getFirstName().equalsIgnoreCase(firstName)
                        && medicalRecordsLooking.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    /**
     * Updates a medical record that matches the given first and last name.
     *
     * @param medicalRecords The medical record containing updated information.
     */
    public void updateMedicalRecords(MedicalRecords medicalRecords) {
        medicalRecordsList.stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(medicalRecords.getFirstName()) &&
                        mr.getLastName().equalsIgnoreCase(medicalRecords.getLastName()))
                .findFirst()
                .ifPresentOrElse(
                        mrToUpdate -> {
                            mrToUpdate.setBirthdate(medicalRecords.getBirthdate());
                            mrToUpdate.setMedications(medicalRecords.getMedications());
                            mrToUpdate.setAllergies(medicalRecords.getAllergies());
                        },
                        () -> logger.warn("Attempted to update non-existing medical record for {} {}",
                                medicalRecords.getFirstName(), medicalRecords.getLastName())
                );
    }

    /**
     * Deletes a medical record by first and last name.
     *
     * @param firstName The first name of the person.
     * @param lastName  The last name of the person.
     */
    public void deleteMedicalRecords(String firstName, String lastName) {
        medicalRecordsList.stream()
                .filter(personLooking -> personLooking.getFirstName().equalsIgnoreCase(firstName)
                        && personLooking.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .ifPresent(medicalRecordsList::remove);
    }

    /**
     * Returns a blank/default medical record object.
     *
     * @return A MedicalRecords instance with empty fields.
     */
    public MedicalRecords blankMedicalRecords() {
        return new MedicalRecords("", "", "", new ArrayList<>(), new ArrayList<>());
    }
}
