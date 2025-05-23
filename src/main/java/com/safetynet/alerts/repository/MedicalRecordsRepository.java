package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.MedicalRecords;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MedicalRecordsRepository {

    private final List<MedicalRecords> medicalRecordsList = new ArrayList<>();


    public List<MedicalRecords> getAllMedicalRecords() {
        return new ArrayList<>(medicalRecordsList);
    }

    public void addMedicalRecords(MedicalRecords medicalRecords) {
        medicalRecordsList.add(medicalRecords);
    }

    public Optional<MedicalRecords> getMedicalRecords(String firstName, String lastName) {
        return medicalRecordsList.stream()
                .filter(medicalRecordsLooking -> medicalRecordsLooking.getFirstName().equalsIgnoreCase(firstName) && medicalRecordsLooking.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    public MedicalRecords updateMedicalRecords(MedicalRecords medicalRecords) {
        return medicalRecordsList.stream()
                .filter(medicalRecordsLooking -> medicalRecordsLooking.getFirstName().equalsIgnoreCase(medicalRecords.getFirstName()) && medicalRecordsLooking.getLastName().equalsIgnoreCase(medicalRecords.getLastName()))
                .findFirst()
                .map(medicalRecordsUpdate -> {
                    medicalRecordsUpdate.setBirthdate(medicalRecords.getBirthdate());
                    medicalRecordsUpdate.setMedications(medicalRecords.getMedications());
                    medicalRecordsUpdate.setAllergies(medicalRecords.getAllergies());

                    return medicalRecordsUpdate;
                })
                .orElse(blankMedicalRecords());
    }

    public void deleteMedicalRecords(String firstName, String lastName) {
        medicalRecordsList.stream()
                .filter(personLooking -> personLooking.getFirstName().equalsIgnoreCase(firstName) && personLooking.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .ifPresent(medicalRecordsList::remove);
    }

    public MedicalRecords blankMedicalRecords() {
        return new MedicalRecords("", "", "", new ArrayList<>(), new ArrayList<>());
    }

}
