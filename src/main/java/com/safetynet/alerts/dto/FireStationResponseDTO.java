package com.safetynet.alerts.dto;

import java.util.List;

public class FireStationResponseDTO {
    private List<FireStationDTO> fireStationDTOList;
    private Integer numberOfAdults;
    private Integer numberOfChildren;

    public FireStationResponseDTO(List<FireStationDTO> fireStationDTOList, Integer numberOfAdults, Integer numberOfChildren) {
        this.fireStationDTOList = fireStationDTOList;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    public List<FireStationDTO> getFireStationDTOList() {
        return fireStationDTOList;
    }

    public void setFireStationDTOList(List<FireStationDTO> fireStationDTOList) {
        this.fireStationDTOList = fireStationDTOList;
    }

    public Integer getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(Integer numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public Integer getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(Integer numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }
}
