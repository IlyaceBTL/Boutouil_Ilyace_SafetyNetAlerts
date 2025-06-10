package com.safetynet.alerts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordsRepository;
import com.safetynet.alerts.repository.PersonRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service responsible for reading initial data from the JSON file and loading it
 * into the application's repositories.
 */
@Service
public class JSONReaderService {

    @Value("${data.path}")
    private String dataPath;

    private static final Logger logger = Logger.getLogger(JSONReaderService.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final PersonRepository personRepository;
    private final FireStationRepository fireStationRepository;
    private final MedicalRecordsRepository medicalRecordsRepository;

    /**
     * Constructor for JSONReaderService, injecting required repositories.
     *
     * @param personRepository         the repository for storing person data
     * @param fireStationRepository    the repository for storing fire station data
     * @param medicalRecordsRepository the repository for storing medical records data
     */
    @Autowired
    private JSONReaderService(PersonRepository personRepository, FireStationRepository fireStationRepository, MedicalRecordsRepository medicalRecordsRepository) {
        this.personRepository = personRepository;
        this.fireStationRepository = fireStationRepository;
        this.medicalRecordsRepository = medicalRecordsRepository;
    }

    /**
     * Initializes the service after Spring context is created.
     * Reads the `data.json` file and loads persons, fire stations, and medical records
     * into memory using their respective repositories.
     */
    @PostConstruct
    public void loadData() {
        try {
            JsonNode root = objectMapper.readTree(new File(dataPath));
            loadPerson(root);
            loadFireStation(root);
            loadMedicalRecords(root);
        } catch (IOException e) {
            logger.info("No data");
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads and parses the "persons" section from the JSON tree,
     * and stores each entry in the PersonRepository.
     *
     * @param root the root node of the parsed JSON structure
     */
    public void loadPerson(JsonNode root) {
        List<Person> personList;
        try {
            JsonNode personsNode = root.get("persons");
            Person[] personsArray = objectMapper.treeToValue(personsNode, Person[].class);
            personList = Arrays.asList(personsArray);
            for (Person person : personList) {
                personRepository.addPerson(person);
            }
            logger.info(personList.size() + " Person(s) loaded from data.json");
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "Error loading person", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads and parses the "firestations" section from the JSON tree,
     * and stores each entry in the FireStationRepository.
     *
     * @param root the root node of the parsed JSON structure
     */
    public void loadFireStation(JsonNode root) {
        List<FireStation> fireStationList;
        try {
            JsonNode fireStationNode = root.get("firestations");
            FireStation[] fireStationTableau = objectMapper.treeToValue(fireStationNode, FireStation[].class);
            fireStationList = Arrays.asList(fireStationTableau);
            for (FireStation fireStation : fireStationList) {
                fireStationRepository.addFireStation(fireStation);
            }
            logger.info(fireStationList.size() + " FireStation(s) loaded from data.json");
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "Error loading FireStation", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads and parses the "medicalrecords" section from the JSON tree,
     * and stores each entry in the MedicalRecordsRepository.
     *
     * @param root the root node of the parsed JSON structure
     */
    public void loadMedicalRecords(JsonNode root) {
        List<MedicalRecords> medicalRecordsList;
        try {
            JsonNode medicalRecordsNode = root.get("medicalrecords");
            MedicalRecords[] medicalRecordsTableau = objectMapper.treeToValue(medicalRecordsNode, MedicalRecords[].class);
            medicalRecordsList = Arrays.asList(medicalRecordsTableau);
            for (MedicalRecords medicalRecords : medicalRecordsList) {
                medicalRecordsRepository.addMedicalRecords(medicalRecords);
            }
            logger.info(medicalRecordsList.size() + " MedicalRecord(s) loaded from data.json");
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "Error loading MedicalRecords", e);
            throw new RuntimeException(e);
        }
    }
}
