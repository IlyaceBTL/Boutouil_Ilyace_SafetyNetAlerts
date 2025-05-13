package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireStationDTO;
import com.safetynet.alerts.dto.FireStationResponseDTO;
import com.safetynet.alerts.dto.PhoneAlertDTO;
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

    public FireStationResponseDTO getPersonByStationNumber(String stationNumber) {
        List<FireStationDTO> fireStationDTOList = new ArrayList<>();
        List<Person> personList = personRepository.getAllperson();
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

    public List<PhoneAlertDTO> getPhoneNumberByFireStation(String fireStationNumber) {
        List<PhoneAlertDTO> personByFireStation = new ArrayList<>();
        List<Person> personList = personRepository.getAllperson();
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
}
