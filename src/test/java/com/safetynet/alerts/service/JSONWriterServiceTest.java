package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordsRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Unit tests for JSONWriterService.
 * Tests saving, updating, and deleting operations on FireStation, Person, and MedicalRecords
 * entities, verifying JSON file content updates correctly.
 */
@ExtendWith(MockitoExtension.class)
class JSONWriterServiceTest {

    @InjectMocks
    private JSONWriterService jsonWriterService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FireStationRepository fireStationRepository;

    @Mock
    private MedicalRecordsRepository medicalRecordsRepository;

    private static final String TEMP_FILE_PATH = "test-data.json";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() throws IOException {
        String jsonContent = """
                {
                  "persons": [
                  { "firstName": "John","lastName": "Smith","address": "123 Main St","city": "SomeCity","zip": "12345","phone": "123-456-7890","email": "jane.smith@example.com"}
                  ],
                  "firestations": [
                    { "address": "1509 Culver St", "station": "3" }
                  ],
                  "medicalrecords": [
                  {"firstName": "John","lastName": "Doe","birthdate": "01/01/1990","medications": ["med1"],"allergies": ["allergy1"]}
                  ]
                }
                """;
        Files.writeString(Path.of(TEMP_FILE_PATH), jsonContent);
        jsonWriterService = new JSONWriterService(personRepository, fireStationRepository, medicalRecordsRepository);
        ReflectionTestUtils.setField(jsonWriterService, "dataPath", TEMP_FILE_PATH);
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(TEMP_FILE_PATH));
    }

    /**
     * Test saving a FireStation:
     * replaces existing entry if address matches.
     */
    @Test
    void testSaveFireStation_shouldReplaceExistingStation() throws IOException {
        FireStation updated = new FireStation("1509 Culver St", "4");
        jsonWriterService.saveFireStation(updated);
        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode stations = root.get("firestations");
        assertEquals(1, stations.size());
        assertEquals("4", stations.get(0).get("station").asText());
    }

    /**
     * Test saving a FireStation:
     * adds new entry if address not found.
     */
    @Test
    void testSaveFireStation_shouldAddNewStationIfNotExists() throws IOException {
        FireStation newStation = new FireStation("29 15th St", "2");
        jsonWriterService.saveFireStation(newStation);
        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode stations = root.get("firestations");
        assertEquals(2, stations.size());
        boolean found = false;
        for (JsonNode station : stations) {
            if (station.get("address").asText().equals("29 15th St")) {
                assertEquals("2", station.get("station").asText());
                found = true;
            }
        }
        assertTrue(found);
    }

    /**
     * Test updating a FireStation:
     * updates station number if found.
     */
    @Test
    void testUpdateFireStation_shouldUpdateStationNumber() throws IOException {
        FireStation updated = new FireStation("1509 Culver St", "6");
        when(fireStationRepository.getAllFireStation()).thenReturn(List.of(new FireStation("1509 Culver St", "3")));
        jsonWriterService.updateFireStation(updated);
        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode stations = root.get("firestations");
        assertEquals("6", stations.get(0).get("station").asText());
    }

    /**
     * Test updating a FireStation:
     * does nothing if address not found.
     */
    @Test
    void testUpdateFireStation_shouldDoNothingIfNotFound() throws IOException {
        FireStation updated = new FireStation("Unknown St", "7");
        when(fireStationRepository.getAllFireStation()).thenReturn(List.of(new FireStation("1509 Culver St", "3")));
        jsonWriterService.updateFireStation(updated);
        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode stations = root.get("firestations");
        assertEquals(1, stations.size());
        assertEquals("3", stations.get(0).get("station").asText());
    }

    /**
     * Test deleting a FireStation:
     * removes entry by address.
     */
    @Test
    void testDeleteFireStation_shouldRemoveStation() throws IOException {
        when(fireStationRepository.getAllFireStation()).thenReturn(List.of(new FireStation("1509 Culver St", "3")));
        jsonWriterService.deleteFireStation("1509 Culver St");
        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode stations = root.get("firestations");
        assertEquals(0, stations.size());
    }

    /**
     * Test deleting a FireStation:
     * does nothing if address not found.
     */
    @Test
    void testDeleteFireStation_shouldDoNothingIfAddressNotFound() throws IOException {
        when(fireStationRepository.getAllFireStation()).thenReturn(List.of(new FireStation("1509 Culver St", "3")));
        jsonWriterService.deleteFireStation("Nonexistent St");
        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode stations = root.get("firestations");
        assertEquals(1, stations.size());
        assertEquals("1509 Culver St", stations.get(0).get("address").asText());
    }

    /**
     * Test saving a Person:
     * replaces existing person if names match.
     */
    @Test
    void testSavePerson_shouldReplaceExistingPerson() throws IOException {
        Person updated = new Person("John", "Doe", "newAddress", "newCity", "newZip", "newPhone", "newEmail");

        jsonWriterService.savePerson(updated);

        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode persons = root.get("persons");
        assertEquals(1, persons.size());
        assertEquals("newAddress", persons.get(0).get("address").asText());
    }

    /**
     * Test saving a Person:
     * adds new person if not found.
     */
    @Test
    void testSavePerson_shouldAddNewPersonIfNotExists() throws IOException {
        when(personRepository.getAllPersons()).thenReturn(List.of(new Person("John", "Doe", "address", "city", "zip", "phone", "email")));
        Person newPerson = new Person("Jane", "Smith", "addr2", "city2", "zip2", "phone2", "email2");

        jsonWriterService.savePerson(newPerson);

        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode persons = root.get("persons");
        assertEquals(2, persons.size());

        boolean found = false;
        for (JsonNode person : persons) {
            if (person.get("firstName").asText().equals("Jane") && person.get("lastName").asText().equals("Smith")) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    /**
     * Test updating a Person:
     * updates person details if found.
     */
    @Test
    void testUpdatePerson_shouldUpdateExistingPerson() throws IOException {
        Person updated = new Person("John", "Doe", "updatedAddress", "updatedCity", "updatedZip", "updatedPhone", "updatedEmail");

        when(personRepository.getAllPersons()).thenReturn(List.of(new Person("John", "Doe", "address", "city", "zip", "phone", "email")));

        jsonWriterService.updatePerson(updated);

        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode persons = root.get("persons");
        assertEquals("updatedAddress", persons.get(0).get("address").asText());
    }

    /**
     * Test updating a Person:
     * does nothing if person not found.
     */
    @Test
    void testUpdatePerson_shouldDoNothingIfNotFound() throws IOException {
        Person updated = new Person("Nonexistent", "Person", "addr", "city", "zip", "phone", "email");

        when(personRepository.getAllPersons()).thenReturn(List.of(new Person("John", "Doe", "address", "city", "zip", "phone", "email")));

        jsonWriterService.updatePerson(updated);

        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode persons = root.get("persons");
        assertEquals(1, persons.size());
        assertEquals("John", persons.get(0).get("firstName").asText());
    }

    /**
     * Test deleting a Person:
     * removes person by first and last name.
     */
    @Test
    void testDeletePerson_shouldRemovePerson() throws IOException {
        when(personRepository.getAllPersons()).thenReturn(List.of(new Person("John", "Doe", "address", "city", "zip", "phone", "email")));

        jsonWriterService.deletePerson("John", "Doe");

        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode persons = root.get("persons");
        assertEquals(0, persons.size());
    }

    /**
     * Test deleting a Person:
     * does nothing if person not found.
     */
    @Test
    void testDeletePerson_shouldDoNothingIfNotFound() throws IOException {
        jsonWriterService.deletePerson("Nonexistent", "Person");

        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode persons = root.get("persons");
        assertEquals(1, persons.size());
        assertEquals("John", persons.get(0).get("firstName").asText());
    }

    /**
     * Test saving a MedicalRecord:
     * replaces existing record if names match.
     */
    @Test
    void testSaveMedicalRecord_shouldReplaceExistingRecord() throws IOException {
        MedicalRecords updated = new MedicalRecords("John", "Doe", "01/01/1990", List.of("med2"), List.of("allergy2"));
        jsonWriterService.saveMedicalRecord(updated);

        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode medicalRecords = root.get("medicalrecords");
        assertEquals(1, medicalRecords.size());
        assertEquals("med2", medicalRecords.get(0).get("medications").get(0).asText());
    }

    /**
     * Test saving a MedicalRecord:
     * adds new record if not found.
     */
    @Test
    void testSaveMedicalRecord_shouldAddNewRecordIfNotExists() throws IOException {
        MedicalRecords newRecord = new MedicalRecords("Jane", "Smith", "02/02/1985", List.of("medA"), List.of("allergyA"));
        jsonWriterService.saveMedicalRecord(newRecord);

        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode medicalRecords = root.get("medicalrecords");
        assertEquals(2, medicalRecords.size());

        boolean found = false;
        for (JsonNode medicalRecord : medicalRecords) {
            if (medicalRecord.get("firstName").asText().equals("Jane") && medicalRecord.get("lastName").asText().equals("Smith")) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    /**
     * Test updating a MedicalRecord:
     * updates record details if found.
     */
    @Test
    void testUpdateMedicalRecord_shouldUpdateExistingRecord() throws IOException {
        MedicalRecords updated = new MedicalRecords("John", "Doe", "01/01/1990", List.of("medUpdated"), List.of("allergyUpdated"));

        when(medicalRecordsRepository.getAllMedicalRecords()).thenReturn(List.of(new MedicalRecords("John", "Doe", "01/01/1990", List.of("med1"), List.of("allergy1"))));

        jsonWriterService.updateMedicalRecords(updated);

        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode medicalRecords = root.get("medicalrecords");
        assertEquals("medUpdated", medicalRecords.get(0).get("medications").get(0).asText());
    }

    /**
     * Test updating a MedicalRecord:
     * does nothing if record not found.
     */
    @Test
    void testUpdateMedicalRecord_shouldDoNothingIfNotFound() throws IOException {
        MedicalRecords updated = new MedicalRecords("Nonexistent", "Person", "01/01/1990", List.of(), List.of());

        when(medicalRecordsRepository.getAllMedicalRecords()).thenReturn(List.of(new MedicalRecords("John", "Doe", "01/01/1990", List.of("med1"), List.of("allergy1"))));

        jsonWriterService.updateMedicalRecords(updated);

        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode medicalRecords = root.get("medicalrecords");
        assertEquals(1, medicalRecords.size());
        assertEquals("John", medicalRecords.get(0).get("firstName").asText());
    }

    /**
     * Test deleting a MedicalRecord:
     * removes record by first and last name.
     */
    @Test
    void testDeleteMedicalRecord_shouldRemoveRecord() throws IOException {
        when(medicalRecordsRepository.getAllMedicalRecords()).thenReturn(List.of(new MedicalRecords("John", "Doe", "01/01/1990", List.of("med1"), List.of("allergy1"))));

        jsonWriterService.deleteMedicalRecord("John", "Doe");

        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode medicalRecords = root.get("medicalrecords");
        assertEquals(0, medicalRecords.size());
    }

    /**
     * Test deleting a MedicalRecord:
     * does nothing if record not found.
     */
    @Test
    void testDeleteMedicalRecord_shouldDoNothingIfNotFound() throws IOException {
        when(medicalRecordsRepository.getAllMedicalRecords()).thenReturn(List.of(new MedicalRecords("John", "Doe", "01/01/1990", List.of("med1"), List.of("allergy1"))));

        jsonWriterService.deleteMedicalRecord("Nonexistent", "Person");

        JsonNode root = objectMapper.readTree(new File(TEMP_FILE_PATH));
        JsonNode medicalRecords = root.get("medicalrecords");
        assertEquals(1, medicalRecords.size());
        assertEquals("John", medicalRecords.get(0).get("firstName").asText());
    }
}
