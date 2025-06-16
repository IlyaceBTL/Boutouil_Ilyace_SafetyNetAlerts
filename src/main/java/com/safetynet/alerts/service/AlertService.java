package com.safetynet.alerts.service;

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

/**
 * Service providing alert-related functionalities such as child alerts,
 * fire alerts, and emergency information based on address or station number.
 */
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

    /**
     * Retrieves a list of children (aged 18 or younger) living at the given address,
     * along with a list of other household members.
     *
     * @param address the address to search for children
     * @return list of {@link ChildAlertDTO} representing children and their household
     */
    public List<ChildAlertDTO> getChildByAddress(String address) {
        List<Person> persons = personRepository.getAllPersons();

        List<Person> personSameAddress = persons.stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .toList();

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

    /**
     * Retrieves a list of phone numbers for all residents served by a specific fire station.
     *
     * @param fireStationNumber the fire station number
     * @return list of {@link PhoneAlertDTO} containing phone numbers
     */
    public List<PhoneAlertDTO> getPhoneNumberByFireStation(String fireStationNumber) {
        List<Person> personList = personRepository.getAllPersons();
        List<FireStation> fireStationList = fireStationRepository.getAllFireStation();

        Set<String> coveredAddresses = fireStationList.stream()
                .filter(fs -> fs.getStation().equals(fireStationNumber))
                .map(FireStation::getAddress)
                .collect(Collectors.toSet());

        return personList.stream()
                .filter(p -> coveredAddresses.contains(p.getAddress()))
                .map(p -> new PhoneAlertDTO(p.getPhone()))
                .toList();
    }

    /**
     * Retrieves a list of residents at the specified address along with their medical information
     * and the station number of the fire station serving that address.
     *
     * @param address the address to search
     * @return list of {@link FireDTO} with person and medical information
     */
    public List<FireDTO> getPersonByAddress(String address) {
        FireStation station = fireStationRepository.getFireStationByAddress(address)
                .orElseGet(() -> {
                    logger.warn("No fire station assigned for address: {}", address);
                    return fireStationRepository.blankFireStation();
                });

        return personRepository.getAllPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .map(person -> {
                    MedicalRecords medicalRecords = medicalRecordsService
                            .getMedicalRecordsByName(person.getFirstName(), person.getLastName())
                            .orElseGet(medicalRecordsService::blankMedicalRecords);
                    return new FireDTO(person, station, medicalRecords);
                })
                .toList();
    }

    /**
     * Retrieves information about all residents covered by a list of fire station numbers.
     * Useful for flood alerts.
     *
     * @param fireStationsNumber the list of station numbers
     * @return list of {@link FloodStationsDTO} with person and medical data
     */
    public List<FloodStationsDTO> getPersonByListOfStations(List<String> fireStationsNumber) {
        Set<String> fireStationAddresses = fireStationRepository.getAllFireStation().stream()
                .filter(fs -> fireStationsNumber.contains(fs.getStation()))
                .map(FireStation::getAddress)
                .collect(Collectors.toSet());

        return personRepository.getAllPersons().stream()
                .filter(p -> fireStationAddresses.contains(p.getAddress()))
                .map(p -> {
                    MedicalRecords medicalRecords = medicalRecordsService.getMedicalRecordsByName(p.getFirstName(), p.getLastName())
                            .orElse(medicalRecordsService.blankMedicalRecords());
                    return new FloodStationsDTO(p, medicalRecords);
                })
                .toList();
    }

    /**
     * Retrieves detailed personal and medical information for people with the specified last name.
     *
     * @param lastName the last name to search
     * @return list of {@link PersonInfoDTO} for matching individuals
     */
    public List<PersonInfoDTO> getPersonInfoLastName(String lastName) {
        return personRepository.getAllPersons().stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .map(p -> {
                    MedicalRecords medicalRecords = medicalRecordsService.getMedicalRecordsByName(p.getFirstName(), p.getLastName())
                            .orElse(medicalRecordsService.blankMedicalRecords());
                    return new PersonInfoDTO(p, medicalRecords);
                })
                .toList();
    }

    /**
     * Retrieves the email addresses of all residents living in a given city.
     *
     * @param city the city to search
     * @return list of {@link CommunityEmailDTO} with unique email addresses
     */
    public List<CommunityEmailDTO> getEmailByCity(String city) {
        return personRepository.getAllPersons().stream()
                .filter(p -> p.getCity().equalsIgnoreCase(city))
                .map(p -> new CommunityEmailDTO(p.getEmail()))
                .distinct()
                .toList();
    }

}
