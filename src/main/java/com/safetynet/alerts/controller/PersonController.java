package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.dto.CommunityEmailDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
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
}
