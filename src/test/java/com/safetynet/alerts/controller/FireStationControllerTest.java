package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireStationResponseDTO;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link FireStationController} using Mockito and JUnit 5.
 * This class tests the behavior of FireStationController endpoints including
 * creation, retrieval, update, and deletion of fire station mappings.
 */
@ExtendWith(MockitoExtension.class)
class FireStationControllerTest {

    @Mock
    private FireStationService fireStationService;

    @InjectMocks
    private FireStationController controller;

    /**
     * Test for creating a fire station when it does not already exist.
     * Expects HTTP 201 CREATED.
     */
    @Test
    void createFireStation_shouldReturnCreated() {
        FireStation station = new FireStation("1 Station", "1");
        when(fireStationService.getFireStationByAddress("1 Station")).thenReturn(Optional.empty());
        when(fireStationService.createFireStation(station)).thenReturn(station);

        ResponseEntity<FireStation> response = controller.createFireStation(station);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(station, response.getBody());
    }

    /**
     * Test for creating a fire station that already exists.
     * Expects HTTP 409 CONFLICT.
     */
    @Test
    void createFireStation_shouldReturnConflict() {
        FireStation station = new FireStation("1 Station", "1");
        when(fireStationService.getFireStationByAddress("1 Station")).thenReturn(Optional.of(station));

        ResponseEntity<FireStation> response = controller.createFireStation(station);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    /**
     * Test for retrieving persons by fire station number with valid input.
     * Expects HTTP 200 OK.
     */
    @Test
    void getPersonByStationNumber_shouldReturnOk() {
        FireStationResponseDTO dto = new FireStationResponseDTO(Collections.emptyList(), 0, 0);
        when(fireStationService.getPersonByStationNumber("1")).thenReturn(dto);

        ResponseEntity<FireStationResponseDTO> response = controller.getPersonByStationNumber("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    /**
     * Test for retrieving persons by fire station number with invalid (empty) input.
     * Expects HTTP 400 BAD REQUEST.
     */
    @Test
    void getPersonByStationNumber_shouldReturnBadRequest() {
        ResponseEntity<FireStationResponseDTO> response = controller.getPersonByStationNumber("");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test for updating an existing fire station.
     * Expects HTTP 204 NO CONTENT.
     */
    @Test
    void updateFireStation_shouldReturnNoContent() {
        FireStation station = new FireStation("1 Station", "2");
        when(fireStationService.getFireStationByAddress("1 Station")).thenReturn(Optional.of(station));

        ResponseEntity<FireStation> response = controller.updateFireStation(station);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(fireStationService).updateFireStation(station);
    }

    /**
     * Test for updating a fire station that does not exist.
     * Expects HTTP 404 NOT FOUND.
     */
    @Test
    void updateFireStation_shouldReturnNotFound() {
        FireStation station = new FireStation("1 Station", "2");
        when(fireStationService.getFireStationByAddress("1 Station")).thenReturn(Optional.empty());

        ResponseEntity<FireStation> response = controller.updateFireStation(station);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test for deleting an existing fire station.
     * Expects HTTP 200 OK.
     */
    @Test
    void deleteFireStation_shouldReturnOk() {
        FireStation station = new FireStation("1 Station", "1");
        when(fireStationService.getFireStationByAddress("1 Station")).thenReturn(Optional.of(station));

        ResponseEntity<Void> response = controller.deleteFireStation("1 Station");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(fireStationService).deleteFireStation("1 Station");
    }

    /**
     * Test for deleting a fire station that does not exist.
     * Expects HTTP 404 NOT FOUND.
     */
    @Test
    void deleteFireStation_shouldReturnNotFound() {
        when(fireStationService.getFireStationByAddress("1 Station")).thenReturn(Optional.empty());

        ResponseEntity<Void> response = controller.deleteFireStation("1 Station");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test for deleting a fire station with invalid (empty) input.
     * Expects HTTP 400 BAD REQUEST.
     */
    @Test
    void deleteFireStation_shouldReturnBadRequest() {
        ResponseEntity<Void> response = controller.deleteFireStation("");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
