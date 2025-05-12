package com.safetynet.alerts.dto;

import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class FireStationDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private Integer age;

    public FireStationDTO(String firstName, String lastName, String address, String phone, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.age = age;
    }

    public FireStationDTO(Person person, MedicalRecords medicalRecords) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.address = person.getAddress();
        this.phone = person.getPhone();
        this.age = calculateAge(medicalRecords.getBirthdate());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer calculateAge(String birthDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birth = LocalDate.parse(birthDate, formatter);
        LocalDate now = LocalDate.now();
        return Period.between(birth, now).getYears();
    }
}
