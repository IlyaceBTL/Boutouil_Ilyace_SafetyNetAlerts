package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlertService {

    private final PersonRepository personRepository;
    private final FireStationRepository fireStationRepository;
    private final MedicalRecordsService medicalRecordsService;

    @Autowired
    private AlertService(PersonRepository personRepository, FireStationRepository fireStationRepository, MedicalRecordsService medicalRecordsService) {
        this.personRepository = personRepository;
        this.fireStationRepository = fireStationRepository;
        this.medicalRecordsService = medicalRecordsService;
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

    public List<PhoneAlertDTO> getPhoneNumberByFireStation(String fireStationNumber) {
        List<PhoneAlertDTO> personByFireStation = new ArrayList<>();
        List<Person> personList = personRepository.getAllPersons();
        List<FireStation> fireStationList = fireStationRepository.getAllFireStation();
        for (Person person : personList) {
            for (FireStation fireStation : fireStationList) {
                if (fireStation.getStation().equals(fireStationNumber) && fireStation.getAddress().equals(person.getAddress())) {
                    PhoneAlertDTO phoneAlertDTO = new PhoneAlertDTO(person.getPhone());
                    personByFireStation.add(phoneAlertDTO);
                }
            }
        }
        return personByFireStation;
    }

    public List<FireDTO> getPersonByAddress(String address) {
        List<FireDTO> fireDTOList = new ArrayList<>();
        List<Person> personList = personRepository.getAllPersons();
        List<FireStation> fireStationList = fireStationRepository.getAllFireStation();
        for (Person person : personList) {
            for (FireStation fireStation : fireStationList) {
                if (fireStation.getAddress().equals(address) && person.getAddress().equals(address)) {
                    MedicalRecords medicalRecords = medicalRecordsService.getMedicalRecordsByName(person.getFirstName(), person.getLastName());
                    FireDTO fireDTO = new FireDTO(person, fireStation, medicalRecords);
                    fireDTOList.add(fireDTO);
                }
            }
        }
        return fireDTOList;
    }

    public List<FloodStationsDTO> getPersonByListOfStations(List<String> fireStations) {
        List<FloodStationsDTO> floodStationsDTOList = new ArrayList<>();
        List<Person> personList = personRepository.getAllPersons();
        List<FireStation> fireStationList = fireStationRepository.getAllFireStation();
        for (Person person : personList) {
            for (FireStation fireStation : fireStationList) {
                if (fireStations.contains(fireStation.getStation()) && fireStation.getAddress().equals(person.getAddress())) {
                    MedicalRecords medicalRecords = medicalRecordsService.getMedicalRecordsByName(person.getFirstName(), person.getLastName());
                    FloodStationsDTO floodStationsDTO = new FloodStationsDTO(person, medicalRecords);
                    floodStationsDTOList.add(floodStationsDTO);
                }
            }
        }
        return floodStationsDTOList;
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
