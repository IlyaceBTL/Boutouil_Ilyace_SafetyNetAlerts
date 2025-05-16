package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.dto.CommunityEmailDTO;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final MedicalRecordsService medicalRecordsService;

    @Autowired
    public PersonService(PersonRepository personRepository, MedicalRecordsService medicalRecordsService) {
        this.personRepository = personRepository;
        this.medicalRecordsService = medicalRecordsService;
    }

    public Person createPerson(Person person) {
        personRepository.addPerson(person);
        return person;
    }

    public Person getPerson(String firstName, String lastName) {
        return personRepository.getPerson(firstName, lastName);
    }

    public Person updatePerson(Person person) {
        return personRepository.updatePerson(person);
    }

    public void deletePerson(String firstName, String lastName) {
        personRepository.deletePerson(firstName, lastName);
    }

}
