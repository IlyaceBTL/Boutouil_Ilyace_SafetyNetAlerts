package com.safetynet.alerts.service;

import com.safetynet.alerts.controller.MedicalRecordsController;
import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class AlertService {

    private static final Logger logger = LogManager.getLogger(AlertService.class.getName());

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

        List<Person> persons = personRepository.getAllPersons();

        // Filter persons living at those addresses
        List<Person> personSameAddress = persons.stream()
                .filter(personLooking -> personLooking.getAddress().equals(address))
                .toList();
        // For each person at the address, check if they are a child (age <= 18)
        // If yes, create a ChildAlertDTO including the list of all people at that address
        return personSameAddress.stream()
                .map(person -> {
                    MedicalRecords medicalRecords = medicalRecordsService.getMedicalRecordsByName(person.getFirstName(), person.getLastName())
                            .orElse(medicalRecordsService.blankMedicalRecords());
                    int age = DateUtils.calculateAge(medicalRecords.getBirthdate());
                    if (age <= 18) {
                        return new ChildAlertDTO(person, medicalRecords, personSameAddress);
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

    }

    public List<PhoneAlertDTO> getPhoneNumberByFireStation(String fireStationNumber) {
        List<Person> personList = personRepository.getAllPersons();
        List<FireStation> fireStationList = fireStationRepository.getAllFireStation();

        // Get the addresses covered by the given fire station number
        Set<String> coveredAddresses = fireStationList.stream()
                .filter(fireStation -> fireStation.getStation().equals(fireStationNumber))
                .map(FireStation::getAddress)
                .collect(Collectors.toSet());

        // Filter persons living at those addresses and return their phone numbers
        return personList.stream()
                .filter(person -> coveredAddresses.contains(person.getAddress()))
                .map(person -> new PhoneAlertDTO(person.getPhone()))
                .toList();
    }

    public List<FireDTO> getPersonByAddress(String address) {
        // Retrieve the fire station assignment, or a blank one if none exists
        FireStation station = fireStationRepository
                .getFireStationByAddress(address)
                .orElseGet(() -> {
                    logger.warn("No fire station assigned for address: {}", address);
                    return fireStationRepository.blankFireStation();
                });

        // Filter all persons by matching address
        return personRepository.getAllPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .map(person -> {
                    // Retrieve medical records or blank if absent
                    MedicalRecords medicalRecords = medicalRecordsService
                            .getMedicalRecordsByName(person.getFirstName(), person.getLastName())
                            .orElseGet(medicalRecordsService::blankMedicalRecords);

                    // Build and return the DTO
                    return new FireDTO(person, station, medicalRecords);
                })
                .toList();
    }


    public List<FloodStationsDTO> getPersonByListOfStations(List<String> fireStationsNumber) {
        // Get the set of addresses covered by the provided station numbers
        Set<String> fireStationAddresses = fireStationRepository.getAllFireStation().stream()
                .filter(fireStation -> fireStationsNumber.contains(fireStation.getStation()))
                .map(FireStation::getAddress)
                .collect(Collectors.toSet());

        // Filter persons by those living at one of the fire station addresses
        // Then map them to FloodStationsDTO with their medical records
        return personRepository.getAllPersons().stream()
                .filter(person -> fireStationAddresses.contains(person.getAddress()))
                .map(person -> {
                    MedicalRecords medicalRecords = medicalRecordsService.getMedicalRecordsByName(person.getFirstName(), person.getLastName())
                            .orElse(medicalRecordsService.blankMedicalRecords());
                    return new FloodStationsDTO(person, medicalRecords);
                })
                .toList();

    }

    public List<PersonInfoDTO> getPersonInfoLastName(String lastName) {
        // Get all persons living at the given address with medicalRecords
        return personRepository.getAllPersons().stream()
                .filter(person -> person.getLastName().equals(lastName))
                .map(person -> {
                    MedicalRecords medicalRecords = medicalRecordsService.getMedicalRecordsByName(person.getFirstName(), person.getLastName())
                            .orElse(medicalRecordsService.blankMedicalRecords());
                    return new PersonInfoDTO(person, medicalRecords);
                })
                .toList();
    }

    public List<CommunityEmailDTO> getEmailByCity(String city) {
        return personRepository.getAllPersons().stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(person -> new CommunityEmailDTO(person.getEmail()))
                .distinct()
                .toList();
    }

}
