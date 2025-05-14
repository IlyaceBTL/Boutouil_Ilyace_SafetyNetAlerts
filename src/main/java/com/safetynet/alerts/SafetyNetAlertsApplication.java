package com.safetynet.alerts;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordsRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SafetyNetAlertsApplication {
    private static final Logger logger = LogManager.getLogger(SafetyNetAlertsApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SafetyNetAlertsApplication.class, args);
        PersonRepository personRepository = new PersonRepository();
        FireStationRepository fireStationRepository = new FireStationRepository();
        MedicalRecordsRepository medicalRecordsRepository = new MedicalRecordsRepository();

        List<Person> persons = personRepository.getAllPersons();
        for (Person person : persons) {
            logger.info(person.toString());
        }
        List<FireStation> fireStations = fireStationRepository.getAllFireStation();
        for(FireStation fireStation : fireStations){
            logger.info(fireStation.toString());
        }
        List<MedicalRecords> medicalRecordsList = medicalRecordsRepository.getAllMedicalRecords();
        for(MedicalRecords medicalRecords : medicalRecordsList){
            logger.info(medicalRecords.toString());
        }
    }

}
