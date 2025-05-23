package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FireStationService {

    private final PersonRepository personRepository;
    private final FireStationRepository fireStationRepository;
    private final MedicalRecordsService medicalRecordsService;

    @Autowired
    public FireStationService(PersonRepository personRepository, FireStationRepository fireStationRepository, MedicalRecordsService medicalRecordsService) {
        this.personRepository = personRepository;
        this.fireStationRepository = fireStationRepository;
        this.medicalRecordsService = medicalRecordsService;
    }

    public FireStation createFireStation(FireStation fireStation) {
        fireStationRepository.addFireStation(fireStation);
        return fireStation;
    }

    public FireStation updateFireStation(FireStation fireStation) {
        return fireStationRepository.updateFireStation(fireStation);
    }

    public void deleteFireStation(String address) {
        fireStationRepository.deleteFireStation(address);
    }

    public FireStationResponseDTO getPersonByStationNumber(String stationNumber) {
        List<Person> personList = personRepository.getAllPersons();
        List<FireStation> fireStationList = fireStationRepository.getAllFireStation();

        Set<String> addressesForStation = fireStationList.stream()
                .filter(fs -> fs.getStation().equals(stationNumber))
                .map(FireStation::getAddress)
                .collect(Collectors.toSet());

        List<FireStationDTO> fireStationDTOList = personList.stream()
                .filter(person -> addressesForStation.contains(person.getAddress()))
                .map(person -> {
                    MedicalRecords medicalRecords = medicalRecordsService.getMedicalRecordsByName(person.getFirstName(), person.getLastName())
                            .orElse(medicalRecordsService.blankMedicalRecords());
                    return new FireStationDTO(person, medicalRecords);
                })
                .toList();

        long numberOfAdults = fireStationDTOList.stream()
                .filter(dto -> dto.getAge() > 18)
                .count();
        long numberOfChildren = fireStationDTOList.stream()
                .filter(dto -> dto.getAge() <= 18)
                .count();

        return new FireStationResponseDTO(fireStationDTOList, (int) numberOfAdults, (int) numberOfChildren);
    }


    public FireStation getFireStationByAddress(String address) {
        return fireStationRepository.getFireStationByAddress(address);
    }
}
