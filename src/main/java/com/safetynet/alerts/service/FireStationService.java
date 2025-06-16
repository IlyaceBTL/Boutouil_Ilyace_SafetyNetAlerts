package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service layer for managing fire station assignments and related operations.
 */
@Service
public class FireStationService {

    private final PersonRepository personRepository;
    private final FireStationRepository fireStationRepository;
    private final MedicalRecordsService medicalRecordsService;
    private final JSONWriterService jsonWriterService;

    @Autowired
    public FireStationService(PersonRepository personRepository, FireStationRepository fireStationRepository, MedicalRecordsService medicalRecordsService, JSONWriterService jsonWriterService) {
        this.personRepository = personRepository;
        this.fireStationRepository = fireStationRepository;
        this.medicalRecordsService = medicalRecordsService;
        this.jsonWriterService = jsonWriterService;
    }

    /**
     * Creates a new fire station assignment (address + station number).
     *
     * @param fireStation the fire station to add
     * @return the created fire station
     */
    public FireStation createFireStation(FireStation fireStation) {
        jsonWriterService.saveFireStation(fireStation);
        fireStationRepository.addFireStation(fireStation);
        return fireStation;
    }

    /**
     * Updates the station number assigned to an address.
     *
     * @param fireStation the fire station with updated data
     */
    public void updateFireStation(FireStation fireStation) {
        jsonWriterService.updateFireStation(fireStation);
        fireStationRepository.updateFireStation(fireStation);
    }

    /**
     * Deletes a fire station assignment for a specific address.
     *
     * @param address the address for which the fire station should be removed
     */
    public void deleteFireStation(String address) {
        jsonWriterService.deleteFireStation(address);
        fireStationRepository.deleteFireStation(address);
    }

    /**
     * Retrieves all persons covered by a given fire station number.
     * Also returns the number of adults and children among them.
     *
     * @param stationNumber the fire station number to search for
     * @return a {@link FireStationResponseDTO} containing a list of people,
     * and counts of adults and children
     */
    public FireStationResponseDTO getPersonByStationNumber(String stationNumber) {
        List<Person> personList = personRepository.getAllPersons();
        List<FireStation> fireStationList = fireStationRepository.getAllFireStation();

        // Get all addresses covered by the given station
        Set<String> addressesForStation = fireStationList.stream().filter(fs -> fs.getStation().equals(stationNumber)).map(FireStation::getAddress).collect(Collectors.toSet());

        // Get persons living at these addresses and attach medical info
        List<FireStationDTO> fireStationDTOList = personList.stream().filter(person -> addressesForStation.contains(person.getAddress())).map(person -> {
            MedicalRecords medicalRecords = medicalRecordsService.getMedicalRecordsByName(person.getFirstName(), person.getLastName()).orElse(medicalRecordsService.blankMedicalRecords());
            return new FireStationDTO(person, medicalRecords);
        }).toList();

        // Count adults and children
        long numberOfAdults = fireStationDTOList.stream().filter(dto -> dto.getAge() > 18).count();
        long numberOfChildren = fireStationDTOList.stream().filter(dto -> dto.getAge() <= 18 && dto.getAge() >= 0).count();

        return new FireStationResponseDTO(fireStationDTOList, (int) numberOfAdults, (int) numberOfChildren);
    }

    /**
     * Retrieves the fire station assigned to a specific address.
     *
     * @param address the address to search for
     * @return an {@link Optional} containing the fire station if found
     */
    public Optional<FireStation> getFireStationByAddress(String address) {
        return fireStationRepository.getFireStationByAddress(address);
    }

    /**
     * Provides a blank fire station object.
     *
     * @return a default, empty {@link FireStation} instance
     */
    public FireStation blankFireStation() {
        return fireStationRepository.blankFireStation();
    }
}
