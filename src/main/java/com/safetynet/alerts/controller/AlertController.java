package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.service.AlertService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that handles emergency-related alerts and information,
 */
@RestController
public class AlertController {

    private final AlertService alertService;
    private static final Logger logger = LogManager.getLogger(AlertController.class.getName());

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    //Request Param au lieu du pathVariable
    /**
     * Retrieves a list of person information by last name.
     *
     * @param lastName the last name to search
     * @return list of {@link PersonInfoDTO} or {@code 400 Bad Request} if missing,
     * {@code 404 Not Found} if none found
     */
    @GetMapping("/personInfolastName={lastName}")
    public ResponseEntity<List<PersonInfoDTO>> getPersonInfo(@PathVariable("lastName") String lastName) {
        if (lastName == null || lastName.isBlank()) {
            logger.error("lastName parameter is missing or blank");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<PersonInfoDTO> personInfoDTOList = alertService.getPersonInfoLastName(lastName);

        if (personInfoDTOList.isEmpty()) {
            logger.info("No persons found with last name: {}", lastName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Retrieved {} person(s) with last name: {}", personInfoDTOList.size(), lastName);
        return new ResponseEntity<>(personInfoDTOList, HttpStatus.OK);
    }

    /**
     * Retrieves children living at a specified address and their household members.
     *
     * @param address the address to search
     * @return list of {@link ChildAlertDTO} or {@code 400 Bad Request} if missing,
     * {@code 404 Not Found} if none found
     */
    @GetMapping("/childAlert")
    public ResponseEntity<List<ChildAlertDTO>> getChildAlertByAddress(@RequestParam String address) {
        if (address == null || address.isBlank()) {
            logger.error("Address parameter is missing or blank ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<ChildAlertDTO> children = alertService.getChildByAddress(address);

        if (children.isEmpty()) {
            logger.info("No children found at address: {}", address);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Retrieved {} child(ren) at address: {}", children.size(), address);
        return new ResponseEntity<>(children, HttpStatus.OK);
    }

    /**
     * Retrieves all email addresses of residents in a given city.
     *
     * @param city the name of the city
     * @return list of {@link CommunityEmailDTO} or {@code 400 Bad Request} if missing,
     * {@code 404 Not Found} if none found
     */
    @GetMapping("/communityEmail")
    public ResponseEntity<List<CommunityEmailDTO>> getEmailByCity(@RequestParam String city) {
        if (city == null || city.isBlank()) {
            logger.error("City parameter is missing or blank");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<CommunityEmailDTO> emails = alertService.getEmailByCity(city);

        if (emails.isEmpty()) {
            logger.info("No emails found in city: {}", city);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Retrieved {} email(s) in city: {}", emails.size(), city);
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }

    /**
     * Retrieves phone numbers of residents served by a specific fire station.
     *
     * @param firestation the fire station number
     * @return list of {@link PhoneAlertDTO} or {@code 400 Bad Request} if missing,
     * {@code 404 Not Found} if none found
     */
    @GetMapping("/phoneAlert")
    public ResponseEntity<List<PhoneAlertDTO>> getPhoneByFireStation(@RequestParam String firestation) {
        if (firestation == null || firestation.isBlank()) {
            logger.error("Firestation parameter is missing or blank");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<PhoneAlertDTO> phones = alertService.getPhoneNumberByFireStation(firestation);

        if (phones.isEmpty()) {
            logger.info("No phone numbers found for firestation: {}", firestation);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Retrieved {} phone number(s) for firestation: {}", phones.size(), firestation);
        return new ResponseEntity<>(phones, HttpStatus.OK);
    }

    /**
     * Retrieves persons living at a specified address with fire station coverage information.
     *
     * @param address the address to search
     * @return list of {@link FireDTO} or {@code 400 Bad Request} if missing,
     * {@code 404 Not Found} if none found
     */
    @GetMapping("/fire")
    public ResponseEntity<List<FireDTO>> getPersonByAddress(@RequestParam String address) {
        if (address == null || address.isBlank()) {
            logger.error("Address parameter is missing or blank");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<FireDTO> fireDTOList = alertService.getPersonByAddress(address);

        if (fireDTOList.isEmpty()) {
            logger.info("No persons found at fire address: {}", address);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Retrieved {} person(s) at fire address: {}", fireDTOList.size(), address);
        return new ResponseEntity<>(fireDTOList, HttpStatus.OK);
    }

    /**
     * Retrieves flood-related data (residents and their medical records) for a list of station numbers.
     *
     * @param stations the list of station numbers
     * @return list of {@link FloodStationsDTO} or {@code 400 Bad Request} if input is invalid,
     * {@code 404 Not Found} if none found
     */
    @GetMapping("/flood/stations")
    public ResponseEntity<List<FloodStationsDTO>> getPersonByListOfStations(@RequestParam List<String> stations) {
        if (stations == null || stations.isEmpty()) {
            logger.error("Stations parameter is missing or empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<FloodStationsDTO> result = alertService.getPersonByListOfStations(stations);

        if (result.isEmpty()) {
            if (logger.isInfoEnabled()) {
                String stationList = String.join(", ", stations);
                logger.info("No data found for stations: {}", stationList);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Retrieved flood alert data for {} station(s)", stations.size());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
