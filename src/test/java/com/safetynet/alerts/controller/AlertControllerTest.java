package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.AlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link AlertController}.
 * This class tests the different alert-related endpoints of the controller
 * using a mocked {@link AlertService} and {@link MockMvc}.
 */
class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private AlertService alertService;

    /**
     * Sets up the test environment by initializing the mocked AlertService
     * and setting up the AlertController with MockMvc.
     */
    @BeforeEach
    void setup() {
        alertService = Mockito.mock(AlertService.class);
        AlertController alertController = new AlertController(alertService);
        mockMvc = MockMvcBuilders.standaloneSetup(alertController).build();
    }

    /**
     * Tests the /personInfoLastName endpoint with a known last name.
     */
    @Test
    void testGetPersonInfo() throws Exception {
        String lastName = "Doe";

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 rue");
        person.setCity("Paris");
        person.setZip("12345");
        person.setPhone("1234567890");
        person.setEmail("john@example.com");

        MedicalRecords medicalRecords = new MedicalRecords();
        medicalRecords.setMedications(List.of("med1"));
        medicalRecords.setAllergies(List.of("all1"));
        medicalRecords.setBirthdate("01/01/1990");

        PersonInfoDTO dto = new PersonInfoDTO(person, medicalRecords);
        when(alertService.getPersonInfoLastName(lastName)).thenReturn(List.of(dto));

        mockMvc.perform(get("/personInfolastName={lastName}", lastName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    /**
     * Tests the /personInfoLastName endpoint with an unknown last name (should return 404).
     */
    @Test
    void testGetPersonInfo_NotFound() throws Exception {
        when(alertService.getPersonInfoLastName("Unknown")).thenReturn(List.of());

        mockMvc.perform(get("/personInfoLastName={lastName}", "Unknown"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the /childAlert endpoint with a known address.
     */
    @Test
    void testGetChildAlert() throws Exception {
        String address = "1509 Culver St";
        ChildAlertDTO child = new ChildAlertDTO("John", "Boyd", 10, List.of());
        when(alertService.getChildByAddress(address)).thenReturn(List.of(child));

        mockMvc.perform(get("/childAlert").param("address", address))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    /**
     * Tests the /childAlert endpoint with an unknown address (should return 404).
     */
    @Test
    void testGetChildAlert_NotFound() throws Exception {
        when(alertService.getChildByAddress("unknown")).thenReturn(List.of());

        mockMvc.perform(get("/childAlert").param("address", "unknown"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the /communityEmail endpoint with a known city.
     */
    @Test
    void testGetCommunityEmail() throws Exception {
        CommunityEmailDTO email = new CommunityEmailDTO("email@example.com");
        when(alertService.getEmailByCity("Paris")).thenReturn(List.of(email));

        mockMvc.perform(get("/communityEmail").param("city", "Paris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("email@example.com"));
    }

    /**
     * Tests the /communityEmail endpoint with an unknown city (should return 404).
     */
    @Test
    void testGetCommunityEmail_NotFound() throws Exception {
        when(alertService.getEmailByCity("unknown")).thenReturn(List.of());

        mockMvc.perform(get("/communityEmail").param("city", "unknown"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the /phoneAlert endpoint with a known fire station number.
     */
    @Test
    void testGetPhoneAlert() throws Exception {
        PhoneAlertDTO phone = new PhoneAlertDTO("123-456-7890");
        when(alertService.getPhoneNumberByFireStation("1")).thenReturn(List.of(phone));

        mockMvc.perform(get("/phoneAlert").param("firestation", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].phone").value("123-456-7890"));
    }

    /**
     * Tests the /phoneAlert endpoint with an unknown fire station number (should return 404).
     */
    @Test
    void testGetPhoneAlert_NotFound() throws Exception {
        when(alertService.getPhoneNumberByFireStation("99")).thenReturn(List.of());

        mockMvc.perform(get("/phoneAlert").param("firestation", "99"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the /fire endpoint with a known address.
     */
    @Test
    void testGetPersonByAddress() throws Exception {
        FireDTO fireDto = new FireDTO(
                "John",
                "Doe",
                "123 rue",
                35,
                "123-456-7890",
                List.of("med1", "med2"),
                List.of("allergy1")
        );
        when(alertService.getPersonByAddress("123 rue")).thenReturn(List.of(fireDto));

        mockMvc.perform(get("/fire").param("address", "123 rue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    /**
     * Tests the /fire endpoint with an unknown address (should return 404).
     */
    @Test
    void testGetPersonByAddress_NotFound() throws Exception {
        when(alertService.getPersonByAddress("unknown")).thenReturn(List.of());

        mockMvc.perform(get("/fire").param("address", "unknown"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the /flood/stations endpoint with known station numbers.
     */
    @Test
    void testGetFloodStations() throws Exception {
        FloodStationsDTO floodDto = new FloodStationsDTO(
                "John",
                "Doe",
                "123 rue",
                35,
                List.of("med1", "med2"),
                List.of("allergy1")
        );
        when(alertService.getPersonByListOfStations(List.of("1", "2"))).thenReturn(List.of(floodDto));

        mockMvc.perform(get("/flood/stations").param("stations", "1", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    /**
     * Tests the /flood/stations endpoint with unknown station numbers (should return 404).
     */
    @Test
    void testGetFloodStations_NotFound() throws Exception {
        when(alertService.getPersonByListOfStations(List.of("99"))).thenReturn(List.of());

        mockMvc.perform(get("/flood/stations").param("stations", "99"))
                .andExpect(status().isNotFound());
    }
}
