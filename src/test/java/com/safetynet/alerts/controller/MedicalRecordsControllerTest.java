package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.service.MedicalRecordsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalRecordsControllerTest {

    @Mock
    private MedicalRecordsService medicalRecordsService;

    @InjectMocks
    private MedicalRecordsController medicalRecordsController;

    private MedicalRecords sampleRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleRecord = new MedicalRecords(
                "John",
                "Doe",
                "01/01/1980",
                List.of("med1", "med2"),
                List.of("allergy1")
        );
    }

    @Test
    void createMedicalRecords_success() {
        when(medicalRecordsService.getMedicalRecordsByName("John", "Doe")).thenReturn(Optional.empty());
        when(medicalRecordsService.createMedicalRecords(sampleRecord)).thenReturn(sampleRecord);

        ResponseEntity<MedicalRecords> response = medicalRecordsController.createMedicalRecords(sampleRecord);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleRecord, response.getBody());
        verify(medicalRecordsService).createMedicalRecords(sampleRecord);
    }

    @Test
    void createMedicalRecords_conflict() {
        when(medicalRecordsService.getMedicalRecordsByName("John", "Doe")).thenReturn(Optional.of(sampleRecord));

        ResponseEntity<MedicalRecords> response = medicalRecordsController.createMedicalRecords(sampleRecord);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(medicalRecordsService, never()).createMedicalRecords(any());
    }

    @Test
    void getMedicalRecords_success() {
        when(medicalRecordsService.getMedicalRecordsByName("John", "Doe")).thenReturn(Optional.of(sampleRecord));

        ResponseEntity<MedicalRecords> response = medicalRecordsController.getMedicalRecords("John", "Doe");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleRecord, response.getBody());
    }

    @Test
    void getMedicalRecords_notFound() {
        when(medicalRecordsService.getMedicalRecordsByName("John", "Doe")).thenReturn(Optional.empty());

        ResponseEntity<MedicalRecords> response = medicalRecordsController.getMedicalRecords("John", "Doe");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getMedicalRecords_badRequest() {
        ResponseEntity<MedicalRecords> response1 = medicalRecordsController.getMedicalRecords(null, "Doe");
        ResponseEntity<MedicalRecords> response2 = medicalRecordsController.getMedicalRecords("John", "");

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
    }

    @Test
    void updateMedicalRecords_success() {
        when(medicalRecordsService.getMedicalRecordsByName("John", "Doe")).thenReturn(Optional.of(sampleRecord));

        ResponseEntity<Void> response = medicalRecordsController.updateMedicalRecords(sampleRecord);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(medicalRecordsService).updateMedicalRecords(sampleRecord);
    }

    @Test
    void updateMedicalRecords_notFound() {
        when(medicalRecordsService.getMedicalRecordsByName("John", "Doe")).thenReturn(Optional.empty());

        ResponseEntity<Void> response = medicalRecordsController.updateMedicalRecords(sampleRecord);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(medicalRecordsService, never()).updateMedicalRecords(any());
    }

    @Test
    void updateMedicalRecords_badRequest() {
        MedicalRecords badRecord = new MedicalRecords(null, "", null, null, null);

        ResponseEntity<Void> response = medicalRecordsController.updateMedicalRecords(badRecord);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(medicalRecordsService, never()).updateMedicalRecords(any());
    }

    @Test
    void deleteMedicalRecords_success() {
        when(medicalRecordsService.getMedicalRecordsByName("John", "Doe")).thenReturn(Optional.of(sampleRecord));

        ResponseEntity<Void> response = medicalRecordsController.deleteMedicalRecords("John", "Doe");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(medicalRecordsService).deleteMedicalRecords("John", "Doe");
    }

    @Test
    void deleteMedicalRecords_notFound() {
        when(medicalRecordsService.getMedicalRecordsByName("John", "Doe")).thenReturn(Optional.empty());

        ResponseEntity<Void> response = medicalRecordsController.deleteMedicalRecords("John", "Doe");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(medicalRecordsService, never()).deleteMedicalRecords(any(), any());
    }

    @Test
    void deleteMedicalRecords_badRequest() {
        ResponseEntity<Void> response1 = medicalRecordsController.deleteMedicalRecords(null, "Doe");
        ResponseEntity<Void> response2 = medicalRecordsController.deleteMedicalRecords("John", "");

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
        verify(medicalRecordsService, never()).deleteMedicalRecords(any(), any());
    }
}
