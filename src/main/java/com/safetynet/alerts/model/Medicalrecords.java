package com.safetynet.alerts.model;

import java.util.List;

public class Medicalrecords {
    String firstName;
    String lastName;
    String birthday;
    List<String> medications;

    public Medicalrecords(String firstName, String lastName, String birthday, List<String> medications) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.medications = medications;

    }

    public Medicalrecords() {
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
