package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * REST controller that handles HTTP requests for managing Person objects.
 */
@RestController
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
     * @return {@code 201 Created} if successful, {@code 409 Conflict} if the person already exists
     */
    //TODO ESSAYER DE FAIRE FONCTIONNER @OPTIONAL + MODIFIER LES URLS SURTOUT DANS ALERTS
    @PostMapping("/person")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        if (personService.getPerson(person.getFirstName(), person.getLastName()) != null) {
            logger.warn("Person already exists");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Person createdPerson = personService.createPerson(person);
        logger.info("Creating person: {} {}", createdPerson.getFirstName(), createdPerson.getLastName());
        return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);

    }

    /**
     * Retrieves a person by their first and last name.
     *
     * @param firstName the person's first name
     * @param lastName  the person's last name
     * @return {@code 200 OK} with the person if found,
     * {@code 400 Bad Request} if parameters are missing or invalid,
     * {@code 404 Not Found} if the person does not exist
     */
    @GetMapping("/person")
    public ResponseEntity<Person> getPerson(@RequestParam String firstName, @RequestParam String lastName) {
        if (firstName.isEmpty() || firstName.isBlank() || lastName.isBlank() || lastName.isEmpty()) {
            logger.error("Params are missing or blank");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Person person = personService.getPerson(firstName, lastName);
        if (person == null) {
            logger.info("Person not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            logger.info("Person retrieved - First name: {}, Last name: {}", person.getFirstName(), person.getLastName());
            return new ResponseEntity<>(person, HttpStatus.OK);
        }
    }

    /**
     * Updates an existing person.
     *
     * @param person the person with updated information
     * @return {@code 200 OK} with the updated person if successful,
     * {@code 404 Not Found} if the person does not exist
     */
    @PutMapping("/person")
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        if (personService.getPerson(person.getFirstName(), person.getLastName()) == null) {
            logger.info("Person not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Person updatedPerson = personService.updatePerson(person);
        logger.info("Person Updated - First name: {}, Last name: {}", person.getFirstName(), person.getLastName());
        return new ResponseEntity<>(updatedPerson, HttpStatus.OK);

    }

    /**
     * Deletes a person by their first and last name.
     *
     * @param firstName the person's first name
     * @param lastName  the person's last name
     * @return {@code 200 OK} if deletion was successful,
     * {@code 400 Bad Request} if parameters are missing or invalid
     */
    @DeleteMapping("/person")
    public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        if (firstName.isEmpty() || firstName.isBlank() || lastName.isBlank() || lastName.isEmpty()) {
            logger.error("Params are missing or blank");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        personService.deletePerson(firstName, lastName);
        logger.info("Person deleted - First name: {}, Last name: {}", firstName, lastName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
