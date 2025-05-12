package com.safetynet.alerts.dto;

import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.DateUtils;

import java.util.List;

public class PersonInfoDTO {
    private String firstName;
    private String lastName;
    private String address;
    private Integer age;
    private String email;
    private List<String> medications;
    private List<String> allergies;

    public PersonInfoDTO(String firstName, String lastName, String address, Integer age, String email, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.age = age;
        this.email = email;
        this.medications = medications;
        this.allergies = allergies;
    }

    public PersonInfoDTO(Person person, MedicalRecords medicalRecords) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.address = person.getAddress();
        this.email = person.getEmail();
        this.age = DateUtils.calculateAge(medicalRecords.getBirthdate());
        this.medications = medicalRecords.getMedications();
        this.allergies = medicalRecords.getAllergies();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

}
