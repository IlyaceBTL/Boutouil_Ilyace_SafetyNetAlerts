package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireDTO;
import com.safetynet.alerts.dto.FireStationResponseDTO;
import com.safetynet.alerts.dto.PhoneAlertDTO;
import com.safetynet.alerts.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FireStationController {

    private final FireStationService fireStationService;

    @Autowired
    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    @GetMapping("/firestation")
    public FireStationResponseDTO getPersonByStationNumber(@RequestParam String station_number) {
        return fireStationService.getPersonByStationNumber(station_number);
    }

    @GetMapping("/phoneAlert")
    public List<PhoneAlertDTO> getPhoneByFireStation(@RequestParam String firestation_number) {
        return fireStationService.getPhoneNumberByFireStation(firestation_number);
    }

    @GetMapping("/fire")
    public List<FireDTO> getPersonByAddress(@RequestParam String address) {
        return fireStationService.getPersonByAddress(address);
    }
}
