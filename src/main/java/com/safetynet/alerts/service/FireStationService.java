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

@Service
public class FireStationService {

    private final PersonRepository personRepository;
    private final FireStationRepository fireStationRepository;
    private final MedicalRecordsService medicalRecordsService;

    @Autowired
    private FireStationService(PersonRepository personRepository, FireStationRepository fireStationRepository, MedicalRecordsService medicalRecordsService) {
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
        List<FireStationDTO> fireStationDTOList = new ArrayList<>();
        List<Person> personList = personRepository.getAllPersons();
        List<FireStation> fireStationList = fireStationRepository.getAllFireStation();
        for (Person person : personList) {
            for (FireStation fireStation : fireStationList) {
                if (person.getAddress().equals(fireStation.getAddress()) && fireStation.getStation().equals(stationNumber)) {
                    MedicalRecords medicalRecords = medicalRecordsService.getMedicalRecordsByName(person.getFirstName(), person.getLastName());
                    FireStationDTO fireStationDTO = new FireStationDTO(person, medicalRecords);
                    fireStationDTOList.add(fireStationDTO);
                }
            }

        }
        Integer numberOfAdults = 0;
        Integer numberOfChildren = 0;
        for (FireStationDTO fireStationDTO : fireStationDTOList) {
            if (fireStationDTO.getAge() > 18) {
                numberOfAdults++;
            } else {
                numberOfChildren++;
            }
        }
        return new FireStationResponseDTO(fireStationDTOList, numberOfAdults, numberOfChildren);
    }

    public FireStation getFireStationByAddress(String address) {
        return fireStationRepository.getFireStationByAddress(address);
    }
}
