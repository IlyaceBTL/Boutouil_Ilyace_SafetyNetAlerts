package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordsRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for saving, updating, and deleting entries in data.json for persons,
 * fire stations, and medical records.
 */
@Service
public class JSONWriterService {

    private static final Logger logger = LogManager.getLogger(JSONWriterService.class);

    private static final String MEDICAL_RECORDS_KEY = "medicalrecords";
    private static final String PERSONS_KEY = "persons";
    private static final String FIRE_STATIONS_KEY = "firestations";


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${data.path}")
    private String dataPath;

    private final PersonRepository personRepository;
    private final FireStationRepository fireStationRepository;
    private final MedicalRecordsRepository medicalRecordsRepository;

    public JSONWriterService(PersonRepository personRepository, FireStationRepository fireStationRepository, MedicalRecordsRepository medicalRecordsRepository) {
        this.personRepository = personRepository;
        this.fireStationRepository = fireStationRepository;
        this.medicalRecordsRepository = medicalRecordsRepository;
    }

    /**
     * Saves or updates a person in the data.json file.
     * @param person the person to save or update
     */
    public void savePerson(Person person) {
        try {
            File file = new File(dataPath);
            JsonNode root = objectMapper.readTree(file);

            List<Person> persons = new ArrayList<>(personRepository.getAllPersons());

            boolean exists = persons.stream()
                    .anyMatch(p -> p.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                            p.getLastName().equalsIgnoreCase(person.getLastName()));

            if (exists) {
                logger.info("Person {} {} already exists in data.json, skipping save.", person.getFirstName(), person.getLastName());
                return;
            }

            persons.add(person);
            ((ObjectNode) root).set(PERSONS_KEY, objectMapper.valueToTree(persons));
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);

            logger.info("Saved person {} {} to data.json", person.getFirstName(), person.getLastName());

        } catch (IOException e) {
            logger.error("Error saving person to data.json", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * Updates a person's information in the data.json file.
     * @param updatedPerson the person with updated details
     */
    public void updatePerson(Person updatedPerson) {
        try {
            File file = new File(dataPath);
            JsonNode root = objectMapper.readTree(file);
            List<Person> persons = new ArrayList<>(personRepository.getAllPersons());

            boolean personFound = false;

            for (int i = 0; i < persons.size(); i++) {
                Person p = persons.get(i);
                if (p.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName()) && p.getLastName().equalsIgnoreCase(updatedPerson.getLastName())) {
                    p.setAddress(updatedPerson.getAddress());
                    p.setCity(updatedPerson.getCity());
                    p.setZip(updatedPerson.getZip());
                    p.setPhone(updatedPerson.getPhone());
                    p.setEmail(updatedPerson.getEmail());
                    persons.set(i, p);
                    personFound = true;
                    break;
                }
            }

            if (!personFound) {
                logger.info("Person not found, update skipped");
                return;
            }

            ((ObjectNode) root).set(PERSONS_KEY, objectMapper.valueToTree(persons));
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
            logger.info("Person updated in data.json");
        } catch (IOException e) {
            logger.error("Error updating person in data.json", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a person from the data.json file by first and last name.
     * @param firstName the first name
     * @param lastName the last name
     */
    public void deletePerson(String firstName, String lastName) {
        List<Person> persons = new ArrayList<>(personRepository.getAllPersons());

        boolean removed = persons.removeIf(p -> p.getFirstName().equalsIgnoreCase(firstName.trim()) && p.getLastName().equalsIgnoreCase(lastName.trim()));

        if (!removed) {
            logger.info("Person not found for firstName: '{}' lastName: '{}'", firstName, lastName);
            return;
        }

        try {
            File file = new File(dataPath);
            JsonNode root = objectMapper.readTree(file);
            ((ObjectNode) root).set(PERSONS_KEY, objectMapper.valueToTree(persons));
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
            logger.info("Person deleted from data.json");
        } catch (IOException e) {
            logger.error("Error deleting person from data.json", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves or updates a fire station in the data.json file.
     * @param fireStation the fire station to save or update
     */
    public void saveFireStation(FireStation fireStation) {
        try {
            File file = new File(dataPath);
            JsonNode root = objectMapper.readTree(file);
            ArrayNode stationsNode = (ArrayNode) root.get(FIRE_STATIONS_KEY);

            ArrayNode updatedStations = objectMapper.createArrayNode();
            for (JsonNode node : stationsNode) {
                boolean sameStation = node.get("address").asText().equalsIgnoreCase(fireStation.getAddress());
                if (!sameStation) {
                    updatedStations.add(node);
                }
            }

            updatedStations.add(objectMapper.valueToTree(fireStation));
            ((ObjectNode) root).set(FIRE_STATIONS_KEY, updatedStations);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
            logger.info("Saved fire station at address {} to data.json", fireStation.getAddress());

        } catch (IOException e) {
            logger.error("Error saving fire station to data.json", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates a fire station's assigned number by address.
     * @param updatedFireStation the fire station with updated data
     */
    public void updateFireStation(FireStation updatedFireStation) {
        try {
            File file = new File(dataPath);
            JsonNode root = objectMapper.readTree(file);
            List<FireStation> fireStations = new ArrayList<>(fireStationRepository.getAllFireStation());

            boolean found = false;

            for (int i = 0; i < fireStations.size(); i++) {
                FireStation fs = fireStations.get(i);
                if (fs.getAddress().equalsIgnoreCase(updatedFireStation.getAddress())) {
                    fs.setStation(updatedFireStation.getStation());
                    fireStations.set(i, fs);
                    found = true;
                    break;
                }
            }

            if (!found) {
                logger.info("FireStation not found, update skipped");
                return;
            }

            ((ObjectNode) root).set(FIRE_STATIONS_KEY, objectMapper.valueToTree(fireStations));
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
            logger.info("FireStation updated in data.json");
        } catch (IOException e) {
            logger.error("Error updating FireStation in data.json", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a fire station by its address from data.json.
     * @param address the address of the station
     */
    public void deleteFireStation(String address) {
        List<FireStation> fireStations = new ArrayList<>(fireStationRepository.getAllFireStation());

        boolean removed = fireStations.removeIf(fs -> fs.getAddress().equalsIgnoreCase(address.trim()));

        if (!removed) {
            logger.info("FireStation not found for address: '{}'", address);
            return;
        }

        try {
            File file = new File(dataPath);
            JsonNode root = objectMapper.readTree(file);
            ((ObjectNode) root).set(FIRE_STATIONS_KEY, objectMapper.valueToTree(fireStations));
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
            logger.info("FireStation deleted from data.json");
        } catch (IOException e) {
            logger.error("Error deleting firestation from data.json", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves or updates a medical record in the data.json file.
     * @param medicalRecords the medical record to save or update
     */
    public void saveMedicalRecord(MedicalRecords medicalRecords) {
        try {
            File file = new File(dataPath);
            JsonNode root = objectMapper.readTree(file);
            ArrayNode recordsNode = (ArrayNode) root.get(MEDICAL_RECORDS_KEY);

            ArrayNode updatedRecords = objectMapper.createArrayNode();
            for (JsonNode node : recordsNode) {
                boolean sameRecord = node.get("firstName").asText().equalsIgnoreCase(medicalRecords.getFirstName()) && node.get("lastName").asText().equalsIgnoreCase(medicalRecords.getLastName());
                if (!sameRecord) {
                    updatedRecords.add(node);
                }
            }

            updatedRecords.add(objectMapper.valueToTree(medicalRecords));
            ((ObjectNode) root).set(MEDICAL_RECORDS_KEY, updatedRecords);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
            logger.info("Saved medical record for {} {} to data.json", medicalRecords.getFirstName(), medicalRecords.getLastName());

        } catch (IOException e) {
            logger.error("Error saving medical record to data.json", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates a medical record in the data.json file.
     * @param updatedMedicalRecords the record to update
     */
    public void updateMedicalRecords(MedicalRecords updatedMedicalRecords) {
        try {
            File file = new File(dataPath);
            JsonNode root = objectMapper.readTree(file);
            List<MedicalRecords> medicalRecordsList = new ArrayList<>(medicalRecordsRepository.getAllMedicalRecords());

            boolean found = false;

            for (int i = 0; i < medicalRecordsList.size(); i++) {
                MedicalRecords mr = medicalRecordsList.get(i);
                if (mr.getFirstName().equalsIgnoreCase(updatedMedicalRecords.getFirstName()) && mr.getLastName().equalsIgnoreCase(updatedMedicalRecords.getLastName())) {
                    mr.setBirthdate(updatedMedicalRecords.getBirthdate());
                    mr.setMedications(updatedMedicalRecords.getMedications());
                    mr.setAllergies(updatedMedicalRecords.getAllergies());
                    medicalRecordsList.set(i, mr);
                    found = true;
                    break;
                }
            }

            if (!found) {
                logger.info("MedicalRecords not found, update skipped");
                return;
            }

            ((ObjectNode) root).set(MEDICAL_RECORDS_KEY, objectMapper.valueToTree(medicalRecordsList));
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
            logger.info("MedicalRecords updated in data.json");
        } catch (IOException e) {
            logger.error("Error updating MedicalRecords in data.json", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a medical record by first and last name from data.json.
     * @param firstName the first name
     * @param lastName the last name
     */
    public void deleteMedicalRecord(String firstName, String lastName) {
        List<MedicalRecords> medicalRecordsList = new ArrayList<>(medicalRecordsRepository.getAllMedicalRecords());

        boolean removed = medicalRecordsList.removeIf(mr -> mr.getFirstName().equalsIgnoreCase(firstName.trim()) && mr.getLastName().equalsIgnoreCase(lastName.trim()));

        if (!removed) {
            logger.info("MedicalRecord not found for firstName: '{}' lastName: '{}'", firstName, lastName);
            return;
        }

        try {
            File file = new File(dataPath);
            JsonNode root = objectMapper.readTree(file);
            ((ObjectNode) root).set(MEDICAL_RECORDS_KEY, objectMapper.valueToTree(medicalRecordsList));
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
            logger.info("MedicalRecord deleted from data.json");
        } catch (IOException e) {
            logger.error("Error deleting medical record from data.json", e);
            throw new RuntimeException(e);
        }
    }
}
