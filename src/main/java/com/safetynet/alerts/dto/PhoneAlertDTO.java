package com.safetynet.alerts.dto;

public class PhoneAlertDTO {
    private String phone;

    public PhoneAlertDTO(String phoneList) {
        this.phone = phoneList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
