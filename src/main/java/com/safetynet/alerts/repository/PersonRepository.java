package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing Person objects in memory.
 */
@Repository
public class PersonRepository {

    private final List<Person> personList = new ArrayList<>();

    /**
     * The in-memory list of all persons.
     */
    public List<Person> getAllPersons() {
        return new ArrayList<>(personList);
    }

    /**
     * Retrieves all persons in the repository.
     */
    public void addPerson(Person person) {
        personList.add(person);
    }

    /**
     * Retrieves a person by first and last name.
     *
     * @param firstName the person's first name
     * @param lastName  the person's last name
     * @return the matched person, or null if not found
     */
    public Person getPerson(String firstName, String lastName) {
        return personList.stream()
                .filter(personLooking -> personLooking.getFirstName().equalsIgnoreCase(firstName) && personLooking.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates an existing person by first and last name with new data.
     * Only address, city, zip, phone, and email can be updated.
     *
     * @param person the updated person object
     * @return the updated person, or null if the person does not exist
     */
    public Person updatePerson(Person person) {
        return personList.stream()
                .filter(personLooking -> personLooking.getFirstName().equalsIgnoreCase(person.getFirstName()) && personLooking.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst()
                .map(personUpdate -> {
                    personUpdate.setAddress(person.getAddress());
                    personUpdate.setCity(person.getCity());
                    personUpdate.setZip(person.getZip());
                    personUpdate.setPhone(person.getPhone());
                    personUpdate.setEmail(person.getEmail());
                    return personUpdate;
                })
                .orElse(null);
    }

    /**
     * Deletes a person from the repository based on first and last name.
     *
     * @param firstName the person's first name
     * @param lastName  the person's last name
     */
    public void deletePerson(String firstName, String lastName) {
        personList.stream()
                .filter(personLooking -> personLooking.getFirstName().equalsIgnoreCase(firstName) && personLooking.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .ifPresent(personList::remove);
    }

}
