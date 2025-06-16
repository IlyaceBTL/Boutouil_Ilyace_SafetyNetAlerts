package com.safetynet.alerts.dto;

import com.safetynet.alerts.model.Person;

public class CommunityEmailDTO {
    private String email;

    public CommunityEmailDTO(String email) {
        this.email = email;
    }

    public CommunityEmailDTO(Person person) {
        this.email = person.getEmail();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
