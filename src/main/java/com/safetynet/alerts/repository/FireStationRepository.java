package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.FireStation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FireStationRepository {

    private final List<FireStation> fireStationList = new ArrayList<>();


    public List<FireStation> getAllFireStation() {
        return new ArrayList<>(fireStationList);
    }

    public void addFireStation(FireStation fireStation) {
        fireStationList.add(fireStation);
    }

    public FireStation getFireStation(String address) {
        return fireStationList.stream()
                .filter(fireStationLooking -> fireStationLooking.getAddress().equalsIgnoreCase(address))
                .findFirst()
                .orElse(null);
    }

    public FireStation updateFireStation(FireStation fireStation) {
        return fireStationList.stream()
                .filter(fireStationLooking -> fireStationLooking.getAddress().equalsIgnoreCase(fireStation.getAddress()))
                .findFirst()
                .map(fireStationUpdate -> {
                    fireStationUpdate.setAddress(fireStation.getAddress());
                    fireStationUpdate.setStation(fireStation.getStation());
                    return fireStationUpdate;
                })
                .orElse(null);
    }

    public void deleteFireStation(String address) {
        fireStationList.stream()
                .filter(fireStationLooking -> fireStationLooking.getAddress().equalsIgnoreCase(address))
                .findFirst()
                .ifPresent(fireStationList::remove);
    }
}
