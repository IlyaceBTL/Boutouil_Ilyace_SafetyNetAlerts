package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for managing Person objects in memory.
 */
@Repository
public class PersonRepository {

    private static final Logger logger = LogManager.getLogger(PersonRepository.class.getName());
    private final List<Person> personList = new ArrayList<>();

    /**
     * Retrieves all persons in the repository.
     *
     * @return a list of all stored persons
     */
    public List<Person> getAllPersons() {
        return new ArrayList<>(personList);
    }

    /**
     * Adds a new person to the repository.
     *
     * @param person the person to add
     */
    public void addPerson(Person person) {
        personList.add(person);
    }

    /**
     * Retrieves a person by first and last name.
     *
     * @param firstName the person's first name
     * @param lastName  the person's last name
     * @return an Optional containing the matched person, or empty if not found
     */
    public Optional<Person> getPerson(String firstName, String lastName) {
        return personList.stream()
                .filter(personLooking -> personLooking.getFirstName().equalsIgnoreCase(firstName) &&
                        personLooking.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    /**
     * Updates an existing person by first and last name with new data.
     * Only address, city, zip, phone, and email fields can be updated.
     *
     * @param person the updated person object
     */
    public void updatePerson(Person person) {
        personList.stream()
                .filter(personLooking -> personLooking.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                        personLooking.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst()
                .ifPresentOrElse(
                        personUpdate -> {
                            personUpdate.setAddress(person.getAddress());
                            personUpdate.setCity(person.getCity());
                            personUpdate.setZip(person.getZip());
                            personUpdate.setPhone(person.getPhone());
                            personUpdate.setEmail(person.getEmail());
                        },
                        () -> logger.warn("Attempted to update non-existing person for {} {}",
                                person.getFirstName(), person.getLastName())
                );
    }

    /**
     * Deletes a person from the repository based on first and last name.
     *
     * @param firstName the person's first name
     * @param lastName  the person's last name
     */
    public void deletePerson(String firstName, String lastName) {
        personList.stream()
                .filter(personLooking -> personLooking.getFirstName().equalsIgnoreCase(firstName) &&
                        personLooking.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .ifPresent(personList::remove);
    }

    /**
     * Returns a blank/default Person object.
     *
     * @return a new Person instance with empty fields
     */
    public Person blankPerson() {
        return new Person("", "", "", "", "", "", "");
    }
}
