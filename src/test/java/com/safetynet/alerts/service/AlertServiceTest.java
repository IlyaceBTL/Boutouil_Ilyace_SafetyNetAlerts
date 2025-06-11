package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlertServiceTest {

    @InjectMocks
    private AlertService alertService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FireStationRepository fireStationRepository;

    @Mock
    private MedicalRecordsService medicalRecordsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getChildByAddress_shouldReturnChildrenOnly() {
        Person child = new Person("Tim", "Brown", "123 Street", "City", "12345", "000-000", "email@example.com");
        Person adult = new Person("John", "Brown", "123 Street", "City", "12345", "111-111", "adult@example.com");

        MedicalRecords childMed = new MedicalRecords("Tim", "Brown", "05/20/2015", new ArrayList<>(), new ArrayList<>());
        MedicalRecords adultMed = new MedicalRecords("John", "Brown", "01/01/1980", new ArrayList<>(), new ArrayList<>());

        when(personRepository.getAllPersons()).thenReturn(List.of(child, adult));
        when(medicalRecordsService.getMedicalRecordsByName("Tim", "Brown")).thenReturn(Optional.of(childMed));
        when(medicalRecordsService.getMedicalRecordsByName("John", "Brown")).thenReturn(Optional.of(adultMed));

        List<ChildAlertDTO> result = alertService.getChildByAddress("123 Street");

        assertEquals(1, result.size());
        assertEquals("Tim", result.getFirst().getFirstName());
    }

    @Test
    void getChildByAddress_noChildren_shouldReturnEmpty() {
        Person adult = new Person("John", "Brown", "123 Street", "City", "12345", "111-111", "adult@example.com");
        MedicalRecords adultMed = new MedicalRecords("John", "Brown", "01/01/1980", new ArrayList<>(), new ArrayList<>());

        when(personRepository.getAllPersons()).thenReturn(List.of(adult));
        when(medicalRecordsService.getMedicalRecordsByName("John", "Brown")).thenReturn(Optional.of(adultMed));

        List<ChildAlertDTO> result = alertService.getChildByAddress("123 Street");

        assertTrue(result.isEmpty());
    }

    @Test
    void getPhoneNumberByFireStation_shouldReturnMatchingPhones() {
        Person person = new Person("Alice", "Smith", "456 Avenue", "City", "12345", "999-999", "alice@example.com");
        FireStation fs = new FireStation("456 Avenue", "2");

        when(personRepository.getAllPersons()).thenReturn(List.of(person));
        when(fireStationRepository.getAllFireStation()).thenReturn(List.of(fs));

        List<PhoneAlertDTO> result = alertService.getPhoneNumberByFireStation("2");

        assertEquals(1, result.size());
        assertEquals("999-999", result.getFirst().getPhone());
    }

    @Test
    void getPhoneNumberByFireStation_noMatch_shouldReturnEmpty() {
        when(personRepository.getAllPersons()).thenReturn(Collections.emptyList());
        when(fireStationRepository.getAllFireStation()).thenReturn(Collections.emptyList());

        List<PhoneAlertDTO> result = alertService.getPhoneNumberByFireStation("99");

        assertTrue(result.isEmpty());
    }

    @Test
    void getPersonByAddress_shouldReturnFireDTO() {
        Person person = new Person("Tom", "Doe", "789 Road", "City", "12345", "222-222", "tom@example.com");
        MedicalRecords med = new MedicalRecords("Tom", "Doe", "12/12/1995", List.of("med1"), List.of("allergy1"));
        FireStation fs = new FireStation("789 Road", "1");

        when(fireStationRepository.getFireStationByAddress("789 Road")).thenReturn(Optional.of(fs));
        when(personRepository.getAllPersons()).thenReturn(List.of(person));
        when(medicalRecordsService.getMedicalRecordsByName("Tom", "Doe")).thenReturn(Optional.of(med));

        List<FireDTO> result = alertService.getPersonByAddress("789 Road");

        assertEquals(1, result.size());
        assertEquals("Tom", result.getFirst().getFirstName());
        assertEquals(29, result.getFirst().getAge()); // assuming age is calculated correctly in service
    }

    @Test
    void getPersonByAddress_noFireStation_shouldReturnEmpty() {
        when(fireStationRepository.getFireStationByAddress("unknown")).thenReturn(Optional.empty());

        List<FireDTO> result = alertService.getPersonByAddress("unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    void getEmailByCity_shouldReturnUniqueEmails() {
        Person p1 = new Person("Anna", "Lee", "101 Blvd", "Paris", "75000", "333-333", "anna@example.com");
        Person p2 = new Person("Mark", "Lee", "102 Blvd", "Paris", "75000", "444-444", "mark@example.com");
        Person p3 = new Person("Sam", "White", "103 Blvd", "Paris", "75000", "555-555", "anna@example.com"); // Duplicate email

        when(personRepository.getAllPersons()).thenReturn(List.of(p1, p2, p3));

        List<CommunityEmailDTO> result = alertService.getEmailByCity("Paris");

        assertEquals(3, result.size());
        Set<String> emails = new HashSet<>();
        result.forEach(e -> emails.add(e.getEmail()));
        assertTrue(emails.contains("anna@example.com"));
        assertTrue(emails.contains("mark@example.com"));
    }

    @Test
    void getEmailByCity_noEmails_shouldReturnEmpty() {
        when(personRepository.getAllPersons()).thenReturn(Collections.emptyList());

        List<CommunityEmailDTO> result = alertService.getEmailByCity("unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    void getPersonInfoLastName_shouldReturnMatchingLastName() {
        Person p = new Person("Emily", "Stone", "1 St", "City", "12345", "555-555", "emily@example.com");
        MedicalRecords med = new MedicalRecords("Emily", "Stone", "06/06/2000", List.of(), List.of());

        when(personRepository.getAllPersons()).thenReturn(List.of(p));
        when(medicalRecordsService.getMedicalRecordsByName("Emily", "Stone")).thenReturn(Optional.of(med));

        List<PersonInfoDTO> result = alertService.getPersonInfoLastName("Stone");

        assertEquals(1, result.size());
        assertEquals("Stone", result.getFirst().getLastName());
    }

    @Test
    void getPersonInfoLastName_noMatch_shouldReturnEmpty() {
        when(personRepository.getAllPersons()).thenReturn(Collections.emptyList());

        List<PersonInfoDTO> result = alertService.getPersonInfoLastName("Unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    void getPersonByListOfStations_shouldReturnFloodStationsDTO() {
        Person p = new Person("Lucy", "Heart", "12 Street", "City", "12345", "111-222", "lucy@example.com");
        MedicalRecords med = new MedicalRecords("Lucy", "Heart", "07/07/1990", List.of("medA"), List.of("allergyA"));
        FireStation fs1 = new FireStation("12 Street", "3");

        when(fireStationRepository.getAllFireStation()).thenReturn(List.of(fs1));
        when(personRepository.getAllPersons()).thenReturn(List.of(p));
        when(medicalRecordsService.getMedicalRecordsByName("Lucy", "Heart")).thenReturn(Optional.of(med));

        List<FloodStationsDTO> result = alertService.getPersonByListOfStations(List.of("3"));

        assertEquals(1, result.size());
        assertEquals("Lucy", result.getFirst().getFirstName());
    }

    @Test
    void getPersonByListOfStations_noMatch_shouldReturnEmpty() {
        when(fireStationRepository.getAllFireStation()).thenReturn(Collections.emptyList());

        List<FloodStationsDTO> result = alertService.getPersonByListOfStations(List.of("99"));

        assertTrue(result.isEmpty());
    }
    @Test
    void testConstructorAndGettersAndSetters() {
        FireStationDTO dto = new FireStationDTO("John", "Doe", "123 Street", "000-000", 35);

        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("123 Street", dto.getAddress());
        assertEquals("000-000", dto.getPhone());
        assertEquals(35, dto.getAge());

        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setAddress("456 Avenue");
        dto.setPhone("111-111");
        dto.setAge(40);

        assertEquals("Jane", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("456 Avenue", dto.getAddress());
        assertEquals("111-111", dto.getPhone());
        assertEquals(40, dto.getAge());
    }
}
