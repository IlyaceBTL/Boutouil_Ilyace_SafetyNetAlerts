package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
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

    public List<PersonInfoDTO> getPersonInfoLastName(String lastName) {

        List<PersonInfoDTO> personInfoDTOList = new ArrayList<>();
        List<Person> persons = personRepository.getAllperson();
        for (Person person : persons) {
            if (person.getLastName().equals(lastName)) {
                MedicalRecords medicalRecords = medicalRecordsService.getMedicalRecordsByName(person.getFirstName(), person.getLastName());
                PersonInfoDTO personInfoDTO = new PersonInfoDTO(person, medicalRecords);
                personInfoDTOList.add(personInfoDTO);
            }
        }
        return (personInfoDTOList);
    }
}
