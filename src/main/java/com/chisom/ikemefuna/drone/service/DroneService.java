package com.chisom.ikemefuna.drone.service;

import com.chisom.ikemefuna.drone.dto.responseDto.DroneResponseDto;
import com.chisom.ikemefuna.drone.model.Drone;
import com.chisom.ikemefuna.drone.dto.requestDto.DroneRequest;
import com.chisom.ikemefuna.drone.dto.requestDto.LoadingRequest;
import com.chisom.ikemefuna.drone.dto.responseDto.DeliveryResponseDto;

import java.util.List;

public interface DroneService {
    Drone registerDrone(DroneRequest droneRequest);
    List<Drone> listAvailableDronesForLoading();
    DeliveryResponseDto loadDroneWithMedication(String serialNumber, LoadingRequest medicationCodes);

    DroneResponseDto checkDroneBatteryLevel(String serialNumber);

    DeliveryResponseDto checkLoadedMedicationForADrone(String serialNumber);

}
