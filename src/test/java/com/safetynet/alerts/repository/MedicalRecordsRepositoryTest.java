package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.MedicalRecords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link MedicalRecordsRepository} class.
 * This test class verifies basic CRUD operations on {@link MedicalRecords} objects.
 */
class MedicalRecordsRepositoryTest {

    private MedicalRecordsRepository repository;

    /**
     * Initializes the repository with sample medical records before each test.
     */
    @BeforeEach
    void setUp() {
        repository = new MedicalRecordsRepository();

        repository.addMedicalRecords(new MedicalRecords(
                "John", "Doe", "01/01/1990",
                List.of("med1:100mg", "med2:200mg"),
                List.of("peanut", "pollen")
        ));

        repository.addMedicalRecords(new MedicalRecords(
                "Jane", "Smith", "15/07/1985",
                List.of("ibuprofen:400mg"),
                List.of()
        ));
    }

    /**
     * Test that retrieves all medical records and checks their contents.
     */
    @Test
    void getAllMedicalRecords() {
        List<MedicalRecords> result = repository.getAllMedicalRecords();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    /**
     * Test adding a new medical record to the repository.
     */
    @Test
    void addMedicalRecords() {
        repository.addMedicalRecords(new MedicalRecords(
                "Alice", "Brown", "30/11/1992",
                List.of("aspirin:500mg"),
                List.of("latex")
        ));

        List<MedicalRecords> result = repository.getAllMedicalRecords();

        assertEquals(3, result.size());
        assertEquals("Alice", result.get(2).getFirstName());
        assertEquals(List.of("latex"), result.get(2).getAllergies());
    }

    /**
     * Test retrieving a specific medical record by first and last name.
     */
    @Test
    void getMedicalRecords() {
        Optional<MedicalRecords> result = repository.getMedicalRecords("John", "Doe");

        assertTrue(result.isPresent());
        assertEquals("01/01/1990", result.get().getBirthdate());
        assertEquals(List.of("med1:100mg", "med2:200mg"), result.get().getMedications());
    }

    /**
     * Test updating an existing medical record and verifying the new values.
     */
    @Test
    void updateMedicalRecords() {
        repository.updateMedicalRecords(new MedicalRecords(
                "Jane", "Smith", "15/07/1985",
                List.of("paracetamol:500mg"),
                List.of("gluten")
        ));

        Optional<MedicalRecords> result = repository.getMedicalRecords("Jane", "Smith");

        assertTrue(result.isPresent());
        assertEquals(List.of("paracetamol:500mg"), result.get().getMedications());
        assertEquals(List.of("gluten"), result.get().getAllergies());
    }

    /**
     * Test deleting a medical record and ensuring it is no longer present in the repository.
     */
    @Test
    void deleteMedicalRecords() {
        repository.deleteMedicalRecords("Jane", "Smith");

        List<MedicalRecords> result = repository.getAllMedicalRecords();
        assertEquals(1, result.size());

        Optional<MedicalRecords> deleted = repository.getMedicalRecords("Jane", "Smith");
        assertTrue(deleted.isEmpty());
    }
}
