package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireStationResponseDTO;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for FireStationService class.
 * These tests check if the service correctly returns people covered by a fire station,
 * and counts how many adults and children are there.
 */
class FireStationServiceTest {

    private PersonRepository personRepository;
    private FireStationRepository fireStationRepository;
    private MedicalRecordsService medicalRecordsService;
    private FireStationService fireStationService;

    /**
     * Setup mocks and service before each test.
     */
    @BeforeEach
    void setUp() {
        personRepository = mock(PersonRepository.class);
        fireStationRepository = mock(FireStationRepository.class);
        medicalRecordsService = mock(MedicalRecordsService.class);
        fireStationService = new FireStationService(personRepository, fireStationRepository, medicalRecordsService);
    }

    /**
     * Test getPersonByStationNumber method.
     * It should return the list of people covered by the fire station number,
     * and count the adults and children correctly.
     */
    @Test
    void testGetPersonByStationNumber() {
        String stationNumber = "1";
        String address = "123 Main St";

        FireStation fireStation = new FireStation(address, stationNumber);
        List<FireStation> fireStations = List.of(fireStation);

        Person adult = new Person("John", "Doe", address, "City", "Zip", "123-456-7890", "john.doe@email.com");
        Person child = new Person("Jane", "Doe", address, "City", "Zip", "123-456-7890", "jane.doe@email.com");
        List<Person> persons = List.of(adult, child);

        MedicalRecords adultRecord = new MedicalRecords("John", "Doe", "12/12/1980", List.of(), List.of());
        MedicalRecords childRecord = new MedicalRecords("Jane", "Doe", "12/12/2015", List.of(), List.of());

        when(fireStationRepository.getAllFireStation()).thenReturn(fireStations);
        when(personRepository.getAllPersons()).thenReturn(persons);
        when(medicalRecordsService.getMedicalRecordsByName("John", "Doe")).thenReturn(Optional.of(adultRecord));
        when(medicalRecordsService.getMedicalRecordsByName("Jane", "Doe")).thenReturn(Optional.of(childRecord));

        FireStationResponseDTO result = fireStationService.getPersonByStationNumber(stationNumber);

        assertThat(result).isNotNull();
        assertThat(result.getNumberOfAdults()).isEqualTo(1);
        assertThat(result.getNumberOfChildren()).isEqualTo(1);
    }
}
