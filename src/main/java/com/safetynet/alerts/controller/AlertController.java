package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.service.FireStationService;
import com.safetynet.alerts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AlertController {

    private final FireStationService fireStationService;
    private final PersonService personService;

    @Autowired
    public AlertController(PersonService personService, FireStationService fireStationService) {
        this.personService = personService;
        this.fireStationService = fireStationService;
    }

    @GetMapping("/personInfolastName")
    public List<PersonInfoDTO> getPersonInfo(@RequestParam String lastName) {
        return personService.getPersonInfoLastName(lastName);
    }

    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildAlertByAddress(@RequestParam String address) {
        return personService.getChildByAddress(address);
    }

    @GetMapping("/communityEmail")
    public List<CommunityEmailDTO> getEmailByCity(@RequestParam String city) {
        return personService.getEmailByCity(city);
    }

    @GetMapping("/phoneAlert")
    public List<PhoneAlertDTO> getPhoneByFireStation(@RequestParam String firestation_number) {
        return fireStationService.getPhoneNumberByFireStation(firestation_number);
    }

    @GetMapping("/fire")
    public List<FireDTO> getPersonByAddress(@RequestParam String address) {
        return fireStationService.getPersonByAddress(address);
    }

    @GetMapping("/flood/stations")
    public List<FloodStationsDTO> getPersonByListOfStations(@RequestParam List<String> stations) {
        return fireStationService.getPersonByListOfStations(stations);
    }
}
