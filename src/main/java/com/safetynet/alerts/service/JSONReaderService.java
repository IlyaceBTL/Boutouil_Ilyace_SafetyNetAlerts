package com.safetynet.alerts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordsRepository;
import com.safetynet.alerts.repository.PersonRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class JSONReaderService {


    private static final Logger logger = Logger.getLogger(JSONReaderService.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final PersonRepository personRepository;
    private final FireStationRepository fireStationRepository;
    private final MedicalRecordsRepository medicalRecordsRepository;

    @Autowired
    private JSONReaderService(PersonRepository personRepository, FireStationRepository fireStationRepository, MedicalRecordsRepository medicalRecordsRepository) {
        this.personRepository = personRepository;
        this.fireStationRepository = fireStationRepository;
        this.medicalRecordsRepository = medicalRecordsRepository;
    }

    @PostConstruct
    public void loadData() {
        String filepath = "src/main/resources/data.json";
        try {
            JsonNode root = objectMapper.readTree(new File(filepath));
            loadPerson(root);
            loadFireStation(root);
            loadMedicalRecords(root);
        } catch (IOException e) {
            logger.info("No data");
            throw new RuntimeException(e);
        }
    }

    public void loadPerson(JsonNode root) {
        List<Person> personList;
        try {
            JsonNode personsNode = root.get("persons");
            Person[] personsArray = objectMapper.treeToValue(personsNode, Person[].class);
            personList = Arrays.asList(personsArray);
            for (Person person : personList) {
                personRepository.addPerson(person);

            }
            logger.info(personList.size() + " Person load from data.json");
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "Error loading person", e);
            throw new RuntimeException(e);
        }

    }

    public void loadFireStation(JsonNode root) {
        List<FireStation> fireStationList;
        try {
            JsonNode fireStationNode = root.get("firestations");
            FireStation[] fireStationTableau = objectMapper.treeToValue(fireStationNode, FireStation[].class);
            fireStationList = Arrays.asList(fireStationTableau);
            for (FireStation fireStation : fireStationList) {
                fireStationRepository.addFireStation(fireStation);
            }
            logger.info(fireStationList.size() + " FireStation load from data.json");
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "Error loading FireStation", e);
            throw new RuntimeException(e);
        }
    }

    public void loadMedicalRecords(JsonNode root) {
        List<MedicalRecords> medicalRecordsList;
        try {
            JsonNode medicalRecordsNode = root.get("medicalrecords");
            MedicalRecords[] medicalRecordsTableau = objectMapper.treeToValue(medicalRecordsNode, MedicalRecords[].class);
            medicalRecordsList = Arrays.asList(medicalRecordsTableau);
            for (MedicalRecords medicalRecords : medicalRecordsList) {
                medicalRecordsRepository.addMedicalRecords(medicalRecords);
            }
            logger.info(medicalRecordsList.size() + " MedicalRecords loaded from data.json");
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "Error loading MedicalRecords", e);
            throw new RuntimeException(e);
        }

    }

}
