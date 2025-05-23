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
 * REST controller for managing medical records.
 * Provides endpoints to create, retrieve, update, and delete medical records.
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
     * @param medicalRecords The medical record to create.
     * @return {@code 201 Created} if successful,
     * {@code 409 Conflict} if the record already exists.
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
     * @param firstName The person's first name.
     * @param lastName  The person's last name.
     * @return {@code 200 OK} and the medical record if found,
     * {@code 400 Bad Request} if parameters are missing or invalid,
     * {@code 404 Not Found} if the record doesn't exist.
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
     * @param medicalRecords The updated medical record object.
     * @return {@code 204 No Content} if update is successful,
     * {@code 400 Bad Request} if input parameters are missing or invalid,
     * {@code 404 Not Found} if no matching record exists.
     */
    @PutMapping("/medicalRecord")
    public ResponseEntity<Void> updateMedicalRecords(@RequestBody MedicalRecords medicalRecords) {
        if (medicalRecords.getFirstName() == null || medicalRecords.getFirstName().isBlank() ||
                medicalRecords.getLastName() == null || medicalRecords.getLastName().isBlank()) {
            logger.error("Missing or blank firstName/lastName");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<MedicalRecords> existingMedicalRecord = medicalRecordsService.getMedicalRecordsByName(medicalRecords.getFirstName(), medicalRecords.getLastName());

        if (existingMedicalRecord.isEmpty()) {
            logger.info("Medical record not found for {} {}", medicalRecords.getFirstName(), medicalRecords.getLastName());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        medicalRecordsService.updateMedicalRecords(medicalRecords);
        logger.info("Updated medical record for {} {}", medicalRecords.getFirstName(), medicalRecords.getLastName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes a medical record by first and last name.
     *
     * @param firstName The person's first name.
     * @param lastName  The person's last name.
     * @return {@code 200 OK} if the record was successfully deleted,
     * {@code 400 Bad Request} if input parameters are missing or invalid.
     * {@code 404 Not Found} if the record doesn't exist.
     */
    @DeleteMapping("/medicalRecord")
    public ResponseEntity<Void> deleteMedicalRecords(@RequestParam String firstName, @RequestParam String lastName) {
        if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            logger.error("Params are missing or blank");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<MedicalRecords> existingMedicalRecords = medicalRecordsService.getMedicalRecordsByName(firstName, lastName);
        if (existingMedicalRecords.isEmpty()) {
            logger.info("Medical record not existing for {} {}", firstName, lastName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        medicalRecordsService.deleteMedicalRecords(firstName, lastName);
        logger.info("Deleted medical record for {} {}", firstName, lastName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
