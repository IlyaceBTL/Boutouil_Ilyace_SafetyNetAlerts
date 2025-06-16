package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class that provides operations for managing persons.
 */
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final JSONWriterService jsonWriterService;

    @Autowired
    public PersonService(PersonRepository personRepository, JSONWriterService jsonWriterService) {
        this.personRepository = personRepository;
        this.jsonWriterService = jsonWriterService;
    }

    /**
     * Creates and stores a new person.
     *
     * @param person The person to create.
     * @return The created person.
     */
    public Person createPerson(Person person) {
        jsonWriterService.savePerson(person);
        personRepository.addPerson(person);
        return person;
    }

    /**
     * Retrieves a person by the given first and last name.
     *
     * @param firstName The first name of the person.
     * @param lastName  The last name of the person.
     * @return An Optional containing the person if found, or empty otherwise.
     */
    public Optional<Person> getPerson(String firstName, String lastName) {
        return personRepository.getPerson(firstName, lastName);
    }

    /**
     * Updates an existing person.
     *
     * @param person The person with updated data.
     */
    public void updatePerson(Person person) {
        jsonWriterService.updatePerson(person);
        personRepository.updatePerson(person);
    }

    /**
     * Deletes a person by first and last name.
     *
     * @param firstName The first name of the person.
     * @param lastName  The last name of the person.
     */
    public void deletePerson(String firstName, String lastName) {
        jsonWriterService.deletePerson(firstName, lastName);
        personRepository.deletePerson(firstName, lastName);
    }

    /**
     * Returns a blank/default person object.
     *
     * @return A default Person object with empty or placeholder fields.
     */
    public Person blankPerson() {
        return personRepository.blankPerson();
    }
}
