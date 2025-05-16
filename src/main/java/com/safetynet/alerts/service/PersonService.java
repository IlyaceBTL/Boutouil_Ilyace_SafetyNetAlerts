package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person createPerson(Person person) {
        personRepository.addPerson(person);
        return person;
    }

    public Person getPerson(String firstName, String lastName) {
        return personRepository.getPerson(firstName, lastName);
    }

    public Person updatePerson(Person person) {
        return personRepository.updatePerson(person);
    }

    public void deletePerson(String firstName, String lastName) {
        personRepository.deletePerson(firstName, lastName);
    }

}
