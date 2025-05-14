package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.FireStation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FireStationRepository {

    private final List<FireStation> fireStationList = new ArrayList<>();


    public List<FireStation> getAllFireStation(){
        return new ArrayList<>(fireStationList);
    }

    public void addFireStationRepository(FireStation fireStation) {
        fireStationList.add(fireStation);
    }
}
