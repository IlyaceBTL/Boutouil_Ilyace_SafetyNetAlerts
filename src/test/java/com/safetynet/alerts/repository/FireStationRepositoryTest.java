package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.FireStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link FireStationRepository}.
 * These tests validate CRUD operations and data retrieval
 * for managing {@link FireStation} entities in memory.
 */
class FireStationRepositoryTest {

    @Mock
    private FireStationRepository repository;

    /**
     * Sets up the repository with two default fire stations before each test.
     */
    @BeforeEach
    void setUp() {
        repository = new FireStationRepository();
        repository.addFireStation(new FireStation("1 Station", "1"));
        repository.addFireStation(new FireStation("2 Station", "2"));
    }

    /**
     * Test retrieving all fire stations.
     * Expects a list of size 2 with correct address values.
     */
    @Test
    void getAllFireStation() {
        List<FireStation> result = repository.getAllFireStation();

        assertEquals(2, result.size());
        assertEquals("1 Station", result.getFirst().getAddress());
        assertEquals("2 Station", result.get(1).getAddress());
    }

    /**
     * Test adding a new fire station.
     * Expects the new station to be present and list size to increase.
     */
    @Test
    void addFireStation() {
        repository.addFireStation(new FireStation("3 Station", "3"));
        List<FireStation> result = repository.getAllFireStation();

        assertEquals(3, result.size());
        assertEquals("3 Station", result.get(2).getAddress());
    }

    /**
     * Test updating an existing fire station.
     * Expects the station number to be updated for a known address.
     */
    @Test
    void updateFireStation() {
        repository.updateFireStation(new FireStation("2 Station", "4"));
        List<FireStation> result = repository.getAllFireStation();

        assertEquals("2 Station", result.get(1).getAddress());
        assertEquals("4", result.get(1).getStation());
    }

    /**
     * Test deleting a fire station by address.
     * Expects the station to be removed and not found afterward.
     */
    @Test
    void deleteFireStation() {
        repository.deleteFireStation("2 Station");
        List<FireStation> result = repository.getAllFireStation();

        assertEquals(1, result.size());
        assertThrows(IndexOutOfBoundsException.class, () -> result.get(1));

        Optional<FireStation> fireStation = repository.getFireStationByAddress("2 Station");
        assertTrue(fireStation.isEmpty());
    }

    /**
     * Test retrieving a fire station by address.
     * Expects the correct fire station to be returned with matching station number.
     */
    @Test
    void getFireStationByAddress() {
        Optional<FireStation> result = repository.getFireStationByAddress("1 Station");

        assertTrue(result.isPresent());
        assertEquals("1 Station", result.get().getAddress());
        assertEquals("1", result.get().getStation());
    }

}
