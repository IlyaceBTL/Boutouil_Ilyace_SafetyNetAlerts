package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordsRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JSONReaderServiceTest {

    private PersonRepository personRepository;
    private FireStationRepository fireStationRepository;
    private MedicalRecordsRepository medicalRecordsRepository;
    private JSONReaderService jsonReaderService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        personRepository = mock(PersonRepository.class);
        fireStationRepository = mock(FireStationRepository.class);
        medicalRecordsRepository = mock(MedicalRecordsRepository.class);
        jsonReaderService = new JSONReaderService(personRepository, fireStationRepository, medicalRecordsRepository);
        objectMapper = new ObjectMapper();

        try {
            var field = JSONReaderService.class.getDeclaredField("dataPath");
            field.setAccessible(true);
            field.set(jsonReaderService, "src/test/resources/data.json");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test that loadPerson correctly parses persons from JsonNode and adds them to the repository.
     */
    @Test
    void testLoadPerson() throws Exception {
        String json = """
                {
                  "persons": [
                    {
                      "firstName": "John",
                      "lastName": "Doe",
                      "address": "123 Main St",
                      "city": "City",
                      "zip": "Zip",
                      "phone": "123-456-7890",
                      "email": "john.doe@email.com"
                    }
                  ]
                }
                """;

        JsonNode root = objectMapper.readTree(json);
        jsonReaderService.loadPerson(root);

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository, times(1)).addPerson(captor.capture());

        Person captured = captor.getValue();
        assertThat(captured.getFirstName()).isEqualTo("John");
        assertThat(captured.getLastName()).isEqualTo("Doe");
    }

    /**
     * Test that loadFireStation correctly parses firestations from JsonNode and adds them to the repository.
     */
    @Test
    void testLoadFireStation() throws Exception {
        String json = """
                {
                  "firestations": [
                    {
                      "address": "123 Main St",
                      "station": "1"
                    }
                  ]
                }
                """;

        JsonNode root = objectMapper.readTree(json);
        jsonReaderService.loadFireStation(root);

        ArgumentCaptor<FireStation> captor = ArgumentCaptor.forClass(FireStation.class);
        verify(fireStationRepository, times(1)).addFireStation(captor.capture());

        FireStation captured = captor.getValue();
        assertThat(captured.getAddress()).isEqualTo("123 Main St");
        assertThat(captured.getStation()).isEqualTo("1");
    }

    /**
     * Test that loadMedicalRecords correctly parses medicalrecords from JsonNode and adds them to the repository.
     */
    @Test
    void testLoadMedicalRecords() throws Exception {
        String json = """
                {
                  "medicalrecords": [
                    {
                      "firstName": "John",
                      "lastName": "Doe",
                      "birthdate": "12/12/1980",
                      "medications": [],
                      "allergies": []
                    }
                  ]
                }
                """;

        JsonNode root = objectMapper.readTree(json);
        jsonReaderService.loadMedicalRecords(root);

        ArgumentCaptor<MedicalRecords> captor = ArgumentCaptor.forClass(MedicalRecords.class);
        verify(medicalRecordsRepository, times(1)).addMedicalRecords(captor.capture());

        MedicalRecords captured = captor.getValue();
        assertThat(captured.getFirstName()).isEqualTo("John");
        assertThat(captured.getLastName()).isEqualTo("Doe");
    }

    /**
     * Test that loadData throws RuntimeException if the file is missing or unreadable.
     */
    @Test
    void testLoadDataThrowsException() {
        try {
            var field = JSONReaderService.class.getDeclaredField("dataPath");
            field.setAccessible(true);
            field.set(jsonReaderService, "nonexistent.json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThrows(RuntimeException.class, () -> jsonReaderService.loadData());
    }
}
