package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.FireStation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for managing FireStation objects in memory.
 */
@Repository
public class FireStationRepository {

    private static final Logger logger = LogManager.getLogger(FireStationRepository.class.getName());

    private final List<FireStation> fireStationList = new ArrayList<>();

    /**
     * Retrieves all fire stations in the repository.
     *
     * @return a list of all stored fire stations
     */
    public List<FireStation> getAllFireStation() {
        return new ArrayList<>(fireStationList);
    }

    /**
     * Adds a new fire station to the repository.
     *
     * @param fireStation the fire station to add
     */
    public void addFireStation(FireStation fireStation) {
        fireStationList.add(fireStation);
    }

    /**
     * Updates an existing fire station by address.
     * Only the station number is updated.
     * If the fire station does not exist, logs a warning.
     *
     * @param fireStation the updated fire station object
     */
    public void updateFireStation(FireStation fireStation) {
        fireStationList.stream()
                .filter(fireStationLooking -> fireStationLooking.getAddress().equalsIgnoreCase(fireStation.getAddress()))
                .findFirst()
                .ifPresentOrElse(fireStationUpdate -> fireStationUpdate.setStation(fireStation.getStation()),
                        () -> logger.warn("Attempted to update non-existing FireStation for {}",
                                fireStation.getAddress())
                );
    }

    /**
     * Deletes a fire station from the repository by its address.
     *
     * @param address the address of the fire station to delete
     */
    public void deleteFireStation(String address) {
        fireStationList.stream()
                .filter(fireStationLooking -> fireStationLooking.getAddress().equalsIgnoreCase(address))
                .findFirst()
                .ifPresent(fireStationList::remove);
    }

    /**
     * Retrieves a fire station by address.
     *
     * @param address the address of the fire station
     * @return an Optional containing the fire station if found, or empty otherwise
     */
    public Optional<FireStation> getFireStationByAddress(String address) {
        return fireStationList.stream()
                .filter(fireStationLooking -> fireStationLooking.getAddress().equalsIgnoreCase(address))
                .findFirst();
    }
    /**
     * Returns a blank/default FireStation object.
     *
     * @return a new FireStation instance with empty address and station
     */
    public FireStation blankFireStation() {
        return new FireStation("", "");
    }
}
