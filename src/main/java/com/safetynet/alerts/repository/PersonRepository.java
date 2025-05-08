package com.safetynet.alerts.repository;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Repository
public class PersonRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Person> getAllperson() {
        List<Person> personList;
        try {
            String filepath = "src/main/resources/data.json";
            JsonNode root = objectMapper.readTree(new File(filepath));
            JsonNode personsNode = root.get("persons");
            Person[] personsArray = objectMapper.treeToValue(personsNode, Person[].class);
            personList = Arrays.asList(personsArray);
            return personList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
