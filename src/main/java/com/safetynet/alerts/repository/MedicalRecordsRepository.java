package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.MedicalRecords;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Repository
public class MedicalRecordsRepository {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<MedicalRecords> getAllMedicalRecords() {
        try {
            List<MedicalRecords> allMedicalRecords;
            String filepath = "src/main/resources/data.json";
            JsonNode root = objectMapper.readTree(new File(filepath));
            JsonNode medicalRecordsNode = root.get("medicalrecords");
            MedicalRecords[] medicalRecordsTableau = objectMapper.treeToValue(medicalRecordsNode, MedicalRecords[].class);
            allMedicalRecords = Arrays.asList(medicalRecordsTableau);
            return allMedicalRecords;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
