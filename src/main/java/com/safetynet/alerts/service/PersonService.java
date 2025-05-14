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

    public List<PersonInfoDTO> getPersonInfoLastName(String lastName) {
        List<PersonInfoDTO> personInfoDTOList = new ArrayList<>();
        List<Person> persons = personRepository.getAllPersons();

        for (Person person : persons) {
            if (person.getLastName().equals(lastName)) {
                MedicalRecords medicalRecords = medicalRecordsService.getMedicalRecordsByName(person.getFirstName(), person.getLastName());
                PersonInfoDTO personInfoDTO = new PersonInfoDTO(person, medicalRecords);
                personInfoDTOList.add(personInfoDTO);
            }
        }
        return (personInfoDTOList);
    }

    public List<ChildAlertDTO> getChildByAddress(String address) {
        List<ChildAlertDTO> childAlertDTOList = new ArrayList<>();
        List<Person> persons = personRepository.getAllPersons();

        for (Person person : persons) {
            if (person.getAddress().equals(address)) {
                MedicalRecords medicalRecords = medicalRecordsService.getMedicalRecordsByName(person.getFirstName(), person.getLastName());
                Integer age = DateUtils.calculateAge(medicalRecords.getBirthdate());
                if (age <= 18) {
                    List<Person> personSameAddress = new ArrayList<>();
                    for (Person person1 : persons) {
                        if (person1.getAddress().equals(person.getAddress())) {
                            personSameAddress.add(person1);
                        }
                    }
                    ChildAlertDTO childAlertDTO = new ChildAlertDTO(person, medicalRecords, personSameAddress);
                    childAlertDTOList.add(childAlertDTO);
                }
            }
        }
        return childAlertDTOList;
    }

    public List<CommunityEmailDTO> getEmailByCity(String city) {
        List<CommunityEmailDTO> communityEmailDTOList = new ArrayList<>();
        List<Person> persons = personRepository.getAllPersons();

        for (Person person : persons) {
            if (person.getCity().equals(city)) {
                CommunityEmailDTO communityEmailDTO = new CommunityEmailDTO(person);
                communityEmailDTOList.add(communityEmailDTO);
            }
        }
        return communityEmailDTOList;
    }

}
