package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AlertController {

    private final AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/personInfolastName")
    public List<PersonInfoDTO> getPersonInfo(@RequestParam String lastName) {
        return alertService.getPersonInfoLastName(lastName);
    }

    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildAlertByAddress(@RequestParam String address) {
        return alertService.getChildByAddress(address);
    }

    @GetMapping("/communityEmail")
    public List<CommunityEmailDTO> getEmailByCity(@RequestParam String city) {
        return alertService.getEmailByCity(city);
    }

    @GetMapping("/phoneAlert")
    public List<PhoneAlertDTO> getPhoneByFireStation(@RequestParam String firestation_number) {
        return alertService.getPhoneNumberByFireStation(firestation_number);
    }

    @GetMapping("/fire")
    public List<FireDTO> getPersonByAddress(@RequestParam String address) {
        return alertService.getPersonByAddress(address);
    }

    @GetMapping("/flood/stations")
    public List<FloodStationsDTO> getPersonByListOfStations(@RequestParam List<String> stations) {
        return alertService.getPersonByListOfStations(stations);
    }
}
