package com.safetynet.alerts.dto;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecords;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.DateUtils;

import java.util.List;

public class FireDTO {
    private String firstName;
    private String lastName;
    private String phone;
    private Integer age;
    private String fireStation;
    private List<String> medications;
    private List<String> allergies;

    public FireDTO(String firstName, String lastName, String phone, Integer age, String fireStation, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.age = age;
        this.fireStation = fireStation;
        this.medications = medications;
        this.allergies = allergies;
    }

    public FireDTO(Person person, FireStation fireStation, MedicalRecords medicalRecords) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.phone = person.getPhone();
        this.age = DateUtils.calculateAge(medicalRecords.getBirthdate());
        this.fireStation = fireStation.getStation();
        this.medications = medicalRecords.getMedications();
        this.allergies = medicalRecords.getAllergies();
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFireStation() {
        return fireStation;
    }

    public void setFireStation(String fireStation) {
        this.fireStation = fireStation;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
}
