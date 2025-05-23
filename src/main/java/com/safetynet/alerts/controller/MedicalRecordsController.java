package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.service.MedicalRecordsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller that handles HTTP requests for managing MedicalRecords objects.
 */
@RestController
public class MedicalRecordsController {

    private static final Logger logger = LogManager.getLogger(MedicalRecordsController.class.getName());

    private final MedicalRecordsService medicalRecordsService;


    @Autowired
    public MedicalRecordsController(MedicalRecordsService medicalRecordsService) {
        this.medicalRecordsService = medicalRecordsService;
    }

    /**
     * Creates a new medical record.
     *
     * @param medicalRecords the medical record to create
     * @return {@code 201 Created} if successful, {@code 409 Conflict} if the record already exists
     */
    @PostMapping("/medicalRecord")
    public ResponseEntity<MedicalRecords> createMedicalRecords(@RequestBody MedicalRecords medicalRecords) {
        Optional<MedicalRecords> existingRecord = medicalRecordsService.getMedicalRecordsByName(
                medicalRecords.getFirstName(), medicalRecords.getLastName());

        if (existingRecord.isPresent()) {
            logger.warn("Medical record already exists for {} {}", medicalRecords.getFirstName(), medicalRecords.getLastName());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        MedicalRecords created = medicalRecordsService.createMedicalRecords(medicalRecords);
        logger.info("Created medical record for {} {}", created.getFirstName(), created.getLastName());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    /**
     * Retrieves a medical record by first and last name.
     *
     * @param firstName the person's first name
     * @param lastName  the person's last name
     * @return {@code 200 OK} with the record if found, {@code 400 Bad Request} for invalid params, {@code 404 Not Found} otherwise
     */
    @GetMapping("/medicalRecord")
    public ResponseEntity<MedicalRecords> getMedicalRecords(@RequestParam String firstName, @RequestParam String lastName) {
        if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            logger.error("Params are missing or blank");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<MedicalRecords> optionalMedicalRecords = medicalRecordsService.getMedicalRecordsByName(firstName, lastName);

        if (optionalMedicalRecords.isEmpty()) {
            logger.info("Medical record not found for {} {}", firstName, lastName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        MedicalRecords medicalRecords = optionalMedicalRecords.get();
        logger.info("Retrieved medical record for {} {}", medicalRecords.getFirstName(), medicalRecords.getLastName());
        return new ResponseEntity<>(medicalRecords, HttpStatus.OK);
    }


    /**
     * Updates an existing medical record.
     *
     * @param medicalRecords the updated medical record
     * @return {@code 200 OK} with updated record if successful,
     * {@code 404 Not Found} if the record doesn't exist
     */
    @PutMapping("/medicalRecord")
    public ResponseEntity<MedicalRecords> updateMedicalRecords(@RequestBody MedicalRecords medicalRecords) {
        if (medicalRecordsService.getMedicalRecordsByName(medicalRecords.getFirstName(), medicalRecords.getLastName()) == null || medicalRecords.getLastName().isBlank() || medicalRecords.getFirstName().isBlank()) {
            logger.info("Medical record not found for {} {}", medicalRecords.getFirstName(), medicalRecords.getLastName());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        MedicalRecords updatedMedicalRecords = medicalRecordsService.updtadeMedicalRecords(medicalRecords);
        logger.info("Updated medical record for {} {}", updatedMedicalRecords.getFirstName(), updatedMedicalRecords.getLastName());
        return new ResponseEntity<>(updatedMedicalRecords, HttpStatus.OK);
    }

    /**
     * Deletes a medical record by first and last name.
     *
     * @param firstName the person's first name
     * @param lastName  the person's last name
     * @return {@code 200 OK} if deletion was successful,
     * {@code 400 Bad Request} for invalid parameters
     */
    @DeleteMapping("/medicalRecord")
    public ResponseEntity<Void> deleteMedicalRecords(@RequestParam String firstName, @RequestParam String lastName) {
        if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            logger.error("Params are missing or blank");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        medicalRecordsService.deleteMedicalRecords(firstName, lastName);
        logger.info("Deleted medical record for {} {}", firstName, lastName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
