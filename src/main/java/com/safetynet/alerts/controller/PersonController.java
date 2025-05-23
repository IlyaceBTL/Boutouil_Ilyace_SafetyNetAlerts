package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller that handles HTTP requests for managing Person objects.
 */
@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger logger = LogManager.getLogger(PersonController.class.getName());
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Creates a new person.
     *
     * @param person the person to create
     * @return {@code 201 Created} if successful,
     * {@code 409 Conflict} if the person already exists
     * {@code 400 Bad Request} if parameters are missing or invalid     *
     */
    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        if (person.getLastName() == null || person.getFirstName() == null || person.getFirstName().isBlank() || person.getLastName().isBlank()) {
            logger.error("Params are missing or blank");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Person> existingPerson = personService.getPerson(person.getFirstName(), person.getLastName());
        if (existingPerson.isPresent()) {
            logger.warn("Attempted to create a person that already exists: {} {}", person.getFirstName(), person.getLastName());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Person createdPerson = personService.createPerson(person);
        logger.info("Created person: {} {}", createdPerson.getFirstName(), createdPerson.getLastName());
        return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
    }

    /**
     * Retrieves a person by their first and last name.
     *
     * @param firstName the person's first name
     * @param lastName  the person's last name
     * @return {@code 200 OK} with the person if found,
     * {@code 400 Bad Request} if parameters are invalid,
     * {@code 404 Not Found} if not found
     */
    @GetMapping
    public ResponseEntity<Person> getPerson(@RequestParam String firstName, @RequestParam String lastName) {
        if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            logger.error("Missing or blank parameters in getPerson request");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Person> person = personService.getPerson(firstName, lastName);
        return person.map(value -> {
            logger.info("Retrieved person: {} {}", value.getFirstName(), value.getLastName());
            return new ResponseEntity<>(value, HttpStatus.OK);
        }).orElseGet(() -> {
            logger.info("Person not found: {} {}", firstName, lastName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        });
    }

    /**
     * Updates an existing person.
     *
     * @param person the person with updated data
     * @return {@code 204 No Content} if updated,
     * {@code 400 Bad Request} if parameters are missing or invalid
     * {@code 404 Not Found} if not found
     */
    @PutMapping
    public ResponseEntity<Void> updatePerson(@RequestBody Person person) {
        if (person.getLastName() == null || person.getFirstName() == null || person.getFirstName().isBlank() || person.getLastName().isBlank()) {
            logger.error("Params are missing or blank");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Person> existingPerson = personService.getPerson(person.getFirstName(), person.getLastName());
        if (existingPerson.isEmpty()) {
            logger.info("Cannot update non-existing person: {} {}", person.getFirstName(), person.getLastName());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        personService.updatePerson(person);
        logger.info("Updated person: {} {}", person.getFirstName(), person.getLastName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes a person by their first and last name.
     *
     * @param firstName the person's first name
     * @param lastName  the person's last name
     * @return {@code 204 No Content} if deleted,
     * {@code 400 Bad Request} if parameters are invalid,
     * {@code 404 Not Found} if not found
     */
    @DeleteMapping
    public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            logger.error("Missing or blank parameters in deletePerson request");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Person> personExisting = personService.getPerson(firstName, lastName);
        if (personExisting.isEmpty()) {
            logger.info("Person not found: {} {}", firstName, lastName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        personService.deletePerson(firstName, lastName);
        logger.info("Deleted person: {} {}", firstName, lastName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
