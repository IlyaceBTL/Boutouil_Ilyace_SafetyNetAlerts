package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireStationResponseDTO;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that handles HTTP requests for managing FireStation objects.
 */
@RestController
public class FireStationController {

    private static final Logger logger = LogManager.getLogger(FireStationController.class.getName());

    private final FireStationService fireStationService;

    @Autowired
    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    /**
     * Creates a new fire station assignment (address + station number).
     *
     * @param fireStation the fire station entity to create
     * @return {@code 201 Created} if successful,
     * {@code 409 Conflict} if the assignment already exists
     */
    @PostMapping("/firestation")
    public ResponseEntity<FireStation> createFireStation(@RequestBody FireStation fireStation) {
        if (fireStationService.getFireStationByAddress(fireStation.getAddress()) != null) {
            logger.warn("Fire station already exists at address: {}", fireStation.getAddress());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        FireStation createdFireStation = fireStationService.createFireStation(fireStation);
        logger.info("Created fire station at address: {}, station: {}", createdFireStation.getAddress(), createdFireStation.getStation());
        return new ResponseEntity<>(createdFireStation, HttpStatus.CREATED);
    }

    /**
     * Retrieves all persons covered by a specific fire station number.
     *
     * @param stationNumber the fire station number
     * @return DTO containing list of persons and counts
     */
    @GetMapping("/firestation")
    public ResponseEntity<FireStationResponseDTO> getPersonByStationNumber(@RequestParam("stationNumber") String stationNumber) {
        if (stationNumber == null || stationNumber.isBlank()) {
            logger.error("Station number parameter is missing or blank");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        FireStationResponseDTO response = fireStationService.getPersonByStationNumber(stationNumber);
        logger.info("Retrieved fire station response for station number: {}", stationNumber);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Updates the station number of a fire station assignment.
     *
     * @param fireStation the fire station to update
     * @return {@code 200 OK} with updated object,
     * {@code 404 Not Found} if the address doesn't exist
     */
    @PutMapping("/firestation")
    public ResponseEntity<FireStation> updateFireStation(@RequestBody FireStation fireStation) {
        if (fireStationService.getFireStationByAddress(fireStation.getAddress()) == null) {
            logger.info("Fire station not found at address: {}", fireStation.getAddress());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        FireStation updatedFireStation = fireStationService.updateFireStation(fireStation);
        logger.info("Updated fire station at address: {}, new station: {}", updatedFireStation.getAddress(), updatedFireStation.getStation());
        return new ResponseEntity<>(updatedFireStation, HttpStatus.OK);
    }

    /**
     * Deletes a fire station assignment based on the address.
     *
     * @param address the address of the fire station to delete
     * @return {@code 200 OK} if successful,
     * {@code 400 Bad Request} if address is invalid,
     * {@code 404 Not Found} if not found
     */
    @DeleteMapping("/firestation")
    public ResponseEntity<Void> deleteFireStation(@RequestParam String address) {
        if (address == null || address.isBlank()) {
            logger.error("Address parameter is missing or blank");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (fireStationService.getFireStationByAddress(address) == null) {
            logger.info("Fire station not found at address: {}", address);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        fireStationService.deleteFireStation(address);
        logger.info("Deleted fire station at address: {}", address);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
