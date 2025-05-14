package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PersonRepository {

    private final List<Person> personList = new ArrayList<>();

    public List<Person> getAllPersons() {
        return new ArrayList<>(personList);
    }

    public void addPersonRepository(Person person) {
        personList.add(person);
    }
}
