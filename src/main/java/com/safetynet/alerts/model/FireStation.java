package com.safetynet.alerts.model;

public class FireStation {
    String address;
    String station;

    public FireStation(String address, String station) {
        this.address = address;
        this.station = station;

    }

    public FireStation(){
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
    
    @Override
    public String toString(){
       return this.address+", "+this.station;
    }
}
