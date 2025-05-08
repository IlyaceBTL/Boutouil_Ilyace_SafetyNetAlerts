package com.safetynet.alerts.model;

import java.util.List;

public class MedicalRecords {
    String firstName;
    String lastName;
    String birthdate;
    List<String> medications;
    List<String>allergies;

    public MedicalRecords(String firstName, String lastName, String birthdate, List<String> medications, List<String>allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;

    }

    public MedicalRecords() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    @Override
    public String toString() {
        return this.firstName + ", " + this.lastName + ", " + this.birthdate + ", " + this.medications + ", " + this.allergies;
    }
}
