package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.MedicalRecords;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MedicalRecordsRepository {

    private final List<MedicalRecords> medicalRecordsList = new ArrayList<>();


    public List<MedicalRecords> getAllMedicalRecords() {
        return new ArrayList<>(medicalRecordsList);
    }

    public void addMedicalRecordsRepository(MedicalRecords medicalRecords) {
        medicalRecordsList.add(medicalRecords);
    }
}
