package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireStationResponseDTO;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FireStationControllerTest {

    @Mock
    private FireStationService fireStationService;

    @InjectMocks
    private FireStationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createFireStation_shouldReturnCreated() {
        FireStation station = new FireStation("1 Station", "1");
        when(fireStationService.getFireStationByAddress("1 Station")).thenReturn(Optional.empty());
        when(fireStationService.createFireStation(station)).thenReturn(station);

        ResponseEntity<FireStation> response = controller.createFireStation(station);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(station, response.getBody());
    }

    @Test
    void createFireStation_shouldReturnConflict() {
        FireStation station = new FireStation("1 Station", "1");
        when(fireStationService.getFireStationByAddress("1 Station")).thenReturn(Optional.of(station));

        ResponseEntity<FireStation> response = controller.createFireStation(station);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void getPersonByStationNumber_shouldReturnOk() {
        FireStationResponseDTO dto = new FireStationResponseDTO(Collections.emptyList(), 0, 0);
        when(fireStationService.getPersonByStationNumber("1")).thenReturn(dto);

        ResponseEntity<FireStationResponseDTO> response = controller.getPersonByStationNumber("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getPersonByStationNumber_shouldReturnBadRequest() {
        ResponseEntity<FireStationResponseDTO> response = controller.getPersonByStationNumber("");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateFireStation_shouldReturnNoContent() {
        FireStation station = new FireStation("1 Station", "2");
        when(fireStationService.getFireStationByAddress("1 Station")).thenReturn(Optional.of(station));

        ResponseEntity<FireStation> response = controller.updateFireStation(station);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(fireStationService).updateFireStation(station);
    }

    @Test
    void updateFireStation_shouldReturnNotFound() {
        FireStation station = new FireStation("1 Station", "2");
        when(fireStationService.getFireStationByAddress("1 Station")).thenReturn(Optional.empty());

        ResponseEntity<FireStation> response = controller.updateFireStation(station);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteFireStation_shouldReturnOk() {
        FireStation station = new FireStation("1 Station", "1");
        when(fireStationService.getFireStationByAddress("1 Station")).thenReturn(Optional.of(station));

        ResponseEntity<Void> response = controller.deleteFireStation("1 Station");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(fireStationService).deleteFireStation("1 Station");
    }

    @Test
    void deleteFireStation_shouldReturnNotFound() {
        when(fireStationService.getFireStationByAddress("1 Station")).thenReturn(Optional.empty());

        ResponseEntity<Void> response = controller.deleteFireStation("1 Station");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteFireStation_shouldReturnBadRequest() {
        ResponseEntity<Void> response = controller.deleteFireStation("");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
