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

    public void addPerson(Person person) {
        personList.add(person);
    }

    public Person getPerson(String firstName, String lastName){
        return personList.stream()
                .filter(personLooking -> personLooking.getFirstName().equalsIgnoreCase(firstName) && personLooking.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }

    public Person updatePerson(Person person){
        return personList.stream()
                .filter(personLooking -> personLooking.getFirstName().equalsIgnoreCase(person.getFirstName()) && personLooking.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst()
                .map(personUpdate -> {
                    personUpdate.setAddress(person.getAddress());
                    personUpdate.setCity(person.getCity());
                    personUpdate.setZip(person.getZip());
                    personUpdate.setPhone(person.getPhone());
                    personUpdate.setEmail(person.getEmail());
                    return  personUpdate;
                })
                .orElse(null);
    }

    public void deletePerson(String firstName, String lastName){
        personList.stream()
                .filter(personLooking -> personLooking.getFirstName().equalsIgnoreCase(firstName) && personLooking.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .ifPresent(personList ::remove);
    }

}
