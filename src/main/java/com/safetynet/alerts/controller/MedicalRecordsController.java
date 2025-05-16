package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.service.MedicalRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MedicalRecordsController {

    private final MedicalRecordsService medicalRecordsService;

    @Autowired
    public MedicalRecordsController(MedicalRecordsService medicalRecordsService) {
        this.medicalRecordsService = medicalRecordsService;
    }

    @PostMapping("/medicalRecord")
    public ResponseEntity<MedicalRecords> createMedicalRecords(@RequestBody MedicalRecords medicalRecords) {
        MedicalRecords createdMedicalRecords = medicalRecordsService.createMedicalRecords(medicalRecords);
        return new ResponseEntity<>(createdMedicalRecords, HttpStatus.CREATED);
    }

    @GetMapping("/medicalRecord")
    public ResponseEntity<MedicalRecords> getMedicalRecords(@RequestParam String firstName, @RequestParam String lastName) {
        MedicalRecords medicalRecords = medicalRecordsService.getMedicalRecordsByName(firstName, lastName);
        return new ResponseEntity<>(medicalRecords, HttpStatus.OK);
    }

    @PutMapping("/medicalRecord")
    public ResponseEntity<MedicalRecords> updateMedicalRecords(@RequestBody MedicalRecords medicalRecords) {
        MedicalRecords updatedMedicalRecords = medicalRecordsService.updtadeMedicalRecords(medicalRecords);
        return new ResponseEntity<>(updatedMedicalRecords, HttpStatus.OK);
    }

    @DeleteMapping("/medicalRecord")
    public ResponseEntity<Void> deleteMedicalRecords(@RequestParam String firstName, @RequestParam String lastName) {
        medicalRecordsService.deleteMedicalRecords(firstName, lastName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
