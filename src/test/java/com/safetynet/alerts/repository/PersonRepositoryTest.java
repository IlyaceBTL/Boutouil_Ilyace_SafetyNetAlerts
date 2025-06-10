package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryTest {

    private PersonRepository repository;

    @BeforeEach
    void setUp() {
        repository = new PersonRepository();

        repository.addPerson(new Person("John", "Doe", "123 Main St", "Springfield", "12345", "111-111-1111", "john.doe@example.com"));
        repository.addPerson(new Person("Jane", "Smith", "456 Elm St", "Shelbyville", "67890", "222-222-2222", "jane.smith@example.com"));
    }

    @Test
    void getAllPersons() {
        List<Person> result = repository.getAllPersons();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    @Test
    void addPerson() {
        repository.addPerson(new Person("Alice", "Brown", "789 Oak St", "Capital City", "54321", "333-333-3333", "alice.brown@example.com"));
        List<Person> result = repository.getAllPersons();

        assertEquals(3, result.size());
        assertEquals("Alice", result.get(2).getFirstName());
    }

    @Test
    void getPerson() {
        Optional<Person> result = repository.getPerson("John", "Doe");

        assertTrue(result.isPresent());
        assertEquals("123 Main St", result.get().getAddress());
        assertEquals("Springfield", result.get().getCity());
    }

    @Test
    void updatePerson() {
        repository.updatePerson(new Person("Jane", "Smith", "999 Maple Ave", "New Town", "99999", "444-444-4444", "jane.new@example.com"));
        Optional<Person> result = repository.getPerson("Jane", "Smith");

        assertTrue(result.isPresent());
        assertEquals("999 Maple Ave", result.get().getAddress());
        assertEquals("New Town", result.get().getCity());
        assertEquals("444-444-4444", result.get().getPhone());
        assertEquals("jane.new@example.com", result.get().getEmail());
    }

    @Test
    void deletePerson() {
        repository.deletePerson("Jane", "Smith");

        List<Person> result = repository.getAllPersons();
        assertEquals(1, result.size());
        assertEquals("John", result.getFirst().getFirstName());

        Optional<Person> deleted = repository.getPerson("Jane", "Smith");
        assertTrue(deleted.isEmpty());
    }
}
