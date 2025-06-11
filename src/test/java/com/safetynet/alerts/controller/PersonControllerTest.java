package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonControllerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    private Person samplePerson;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

    @Test
    void createPerson_success() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.empty());
        when(personService.createPerson(samplePerson)).thenReturn(samplePerson);

        ResponseEntity<Person> response = personController.createPerson(samplePerson);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(samplePerson, response.getBody());
        verify(personService).createPerson(samplePerson);
    }

    @Test
    void createPerson_conflict() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.of(samplePerson));

        ResponseEntity<Person> response = personController.createPerson(samplePerson);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(personService, never()).createPerson(any());
    }

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

    @Test
    void getPerson_success() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.of(samplePerson));

        ResponseEntity<Person> response = personController.getPerson("John", "Doe");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(samplePerson, response.getBody());
    }

    @Test
    void getPerson_notFound() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.empty());

        ResponseEntity<Person> response = personController.getPerson("John", "Doe");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getPerson_badRequest() {
        ResponseEntity<Person> response1 = personController.getPerson(null, "Doe");
        ResponseEntity<Person> response2 = personController.getPerson("John", "");

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
    }

    @Test
    void updatePerson_success() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.of(samplePerson));

        ResponseEntity<Void> response = personController.updatePerson(samplePerson);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(personService).updatePerson(samplePerson);
    }

    @Test
    void updatePerson_notFound() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.empty());

        ResponseEntity<Void> response = personController.updatePerson(samplePerson);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(personService, never()).updatePerson(any());
    }

    @Test
    void updatePerson_badRequest() {
        Person badPerson = new Person(null, "", null, null, null, null, null);

        ResponseEntity<Void> response = personController.updatePerson(badPerson);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(personService, never()).updatePerson(any());
    }

    @Test
    void deletePerson_success() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.of(samplePerson));

        ResponseEntity<Void> response = personController.deletePerson("John", "Doe");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(personService).deletePerson("John", "Doe");
    }

    @Test
    void deletePerson_notFound() {
        when(personService.getPerson("John", "Doe")).thenReturn(Optional.empty());

        ResponseEntity<Void> response = personController.deletePerson("John", "Doe");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(personService, never()).deletePerson(any(), any());
    }

    @Test
    void deletePerson_badRequest() {
        ResponseEntity<Void> response1 = personController.deletePerson(null, "Doe");
        ResponseEntity<Void> response2 = personController.deletePerson("John", "");

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
        verify(personService, never()).deletePerson(any(), any());
    }
}
