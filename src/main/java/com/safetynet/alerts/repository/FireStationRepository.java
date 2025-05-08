package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.FireStation;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Repository
public class FireStationRepository {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<FireStation> getAllFireStation(){
        try{
            List<FireStation>allFireStation;
            String filepath = "src/main/resources/data.json";
            JsonNode root = objectMapper.readTree(new File(filepath));
            JsonNode fireStationNode = root.get("firestations");
            FireStation[] fireStationTableau = objectMapper.treeToValue(fireStationNode,FireStation[].class);
            allFireStation = Arrays.asList(fireStationTableau);
            return  allFireStation;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
