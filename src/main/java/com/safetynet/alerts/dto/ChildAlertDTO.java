package com.safetynet.alerts.dto;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.DateUtils;

import java.util.List;

public class ChildAlertDTO {
    private String firstName;
    private String lastName;
    private Integer age;
    List<Person> familyMember;

    public ChildAlertDTO(String firstName, String lastName, Integer age, List<Person> familyMember) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.familyMember = familyMember;
    }

    public ChildAlertDTO(Person person, MedicalRecords medicalRecords, List<Person> familyMember) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.age = DateUtils.calculateAge(medicalRecords.getBirthdate());
        this.familyMember = familyMember;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Person> getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(List<Person> familyMember) {
        this.familyMember = familyMember;
    }
}
