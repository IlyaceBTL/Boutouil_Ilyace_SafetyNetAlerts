package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PersonController}.
 * These tests validate the behavior of the REST endpoints for creating, retrieving,
 * updating, and deleting Person entities.
 */
@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    private Person samplePerson;

    /**
     * Initializes a sample person object used in multiple tests.
     */
    @BeforeEach
    void setUp() {
        samplePerson = new Person(
                "John",
                "Doe",
                "1509 Culver St",
                "Culver",
                "97451",
                "841-874-6512",
                "john.doe@example.com"
        );
    }

    /**
     * Test for successful creation of a new person.
     * Expects HTTP 201 CREATED.
     */
    @Test
    void createPerson_success() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.empty());
        when(personService.createPerson(samplePerson)).thenReturn(samplePerson);

        ResponseEntity<Person> response = personController.createPerson(samplePerson);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(samplePerson, response.getBody());
        verify(personService).createPerson(samplePerson);
    }

    /**
     * Test when trying to create a person that already exists.
     * Expects HTTP 409 CONFLICT.
     */
    @Test
    void createPerson_conflict() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.of(samplePerson));

        ResponseEntity<Person> response = personController.createPerson(samplePerson);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(personService, never()).createPerson(any());
    }

    /**
     * Test for creating a person with invalid data (null or empty fields).
     * Expects HTTP 400 BAD REQUEST.
     */
    @Test
    void createPerson_badRequest() {
        Person badPerson1 = new Person(null, "Doe", null, null, null, null, null);
        Person badPerson2 = new Person("John", "", null, null, null, null, null);

        ResponseEntity<Person> response1 = personController.createPerson(badPerson1);
        ResponseEntity<Person> response2 = personController.createPerson(badPerson2);

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
        verify(personService, never()).createPerson(any());
    }

    /**
     * Test for successfully retrieving an existing person.
     * Expects HTTP 200 OK.
     */
    @Test
    void getPerson_success() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.of(samplePerson));

        ResponseEntity<Person> response = personController.getPerson("John", "Doe");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(samplePerson, response.getBody());
    }

    /**
     * Test when a requested person is not found.
     * Expects HTTP 404 NOT FOUND.
     */
    @Test
    void getPerson_notFound() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.empty());

        ResponseEntity<Person> response = personController.getPerson("John", "Doe");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test when requesting a person with invalid parameters (null or empty).
     * Expects HTTP 400 BAD REQUEST.
     */
    @Test
    void getPerson_badRequest() {
        ResponseEntity<Person> response1 = personController.getPerson(null, "Doe");
        ResponseEntity<Person> response2 = personController.getPerson("John", "");

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
    }

    /**
     * Test for successfully updating an existing person.
     * Expects HTTP 204 NO CONTENT.
     */
    @Test
    void updatePerson_success() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.of(samplePerson));

        ResponseEntity<Void> response = personController.updatePerson(samplePerson);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(personService).updatePerson(samplePerson);
    }

    /**
     * Test when updating a non-existing person.
     * Expects HTTP 404 NOT FOUND.
     */
    @Test
    void updatePerson_notFound() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.empty());

        ResponseEntity<Void> response = personController.updatePerson(samplePerson);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(personService, never()).updatePerson(any());
    }

    /**
     * Test for updating a person with invalid input.
     * Expects HTTP 400 BAD REQUEST.
     */
    @Test
    void updatePerson_badRequest() {
        Person badPerson = new Person(null, "", null, null, null, null, null);

        ResponseEntity<Void> response = personController.updatePerson(badPerson);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(personService, never()).updatePerson(any());
    }

    /**
     * Test for successful deletion of an existing person.
     * Expects HTTP 204 NO CONTENT.
     */
    @Test
    void deletePerson_success() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.of(samplePerson));

        ResponseEntity<Void> response = personController.deletePerson("John", "Doe");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(personService).deletePerson("John", "Doe");
    }

    /**
     * Test when deleting a person that does not exist.
     * Expects HTTP 404 NOT FOUND.
     */
    @Test
    void deletePerson_notFound() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.empty());

        ResponseEntity<Void> response = personController.deletePerson("John", "Doe");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(personService, never()).deletePerson(any(), any());
    }

    /**
     * Test for deleting a person with invalid parameters.
     * Expects HTTP 400 BAD REQUEST.
     */
    @Test
    void deletePerson_badRequest() {
        ResponseEntity<Void> response1 = personController.deletePerson(null, "Doe");
        ResponseEntity<Void> response2 = personController.deletePerson("John", "");

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
        verify(personService, never()).deletePerson(any(), any());
    }
}
