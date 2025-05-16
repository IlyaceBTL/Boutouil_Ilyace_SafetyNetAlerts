package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireStationResponseDTO;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class FireStationController {

    private final FireStationService fireStationService;

    @Autowired
    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    @PostMapping("/firestation")
    public ResponseEntity<FireStation> createFireStation(@RequestBody FireStation fireStation) {
        FireStation createdFireStation = fireStationService.createFireStation(fireStation);
        return new ResponseEntity<>(createdFireStation, HttpStatus.CREATED);
    }

    @GetMapping("/firestation")
    public FireStationResponseDTO getPersonByStationNumber(@RequestParam String station_number) {
        return fireStationService.getPersonByStationNumber(station_number);
    }

    @PutMapping("/firestation")
    public ResponseEntity<FireStation> updateFireStation(@RequestBody FireStation fireStation) {
        FireStation updatedFireStation = fireStationService.updateFireStation(fireStation);
        return new ResponseEntity<>(updatedFireStation, HttpStatus.OK);
    }

    @DeleteMapping("/firestation")
    public ResponseEntity<Void> deleteFireStation(@RequestParam String address) {
        fireStationService.deleteFireStation(address);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
