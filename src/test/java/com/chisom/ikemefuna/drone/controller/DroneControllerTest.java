package com.chisom.ikemefuna.drone.controller;

import com.chisom.ikemefuna.drone.model.Delivery;
import com.chisom.ikemefuna.drone.model.enums.DroneModel;
import com.chisom.ikemefuna.drone.model.enums.DroneState;
import com.chisom.ikemefuna.drone.dto.requestDto.DroneRequest;
import com.chisom.ikemefuna.drone.dto.requestDto.LoadingRequest;
import com.chisom.ikemefuna.drone.dto.responseDto.ApiResponseDto;
import com.chisom.ikemefuna.drone.dto.responseDto.DeliveryResponseDto;
import com.chisom.ikemefuna.drone.dto.responseDto.DroneResponseDto;
import com.chisom.ikemefuna.drone.service.DroneService;
import com.chisom.ikemefuna.drone.model.Drone;
import com.chisom.ikemefuna.drone.model.Medication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class DroneControllerTest {
    @Mock
    private DroneService droneService;
    @InjectMocks
    private DroneController droneController;


    @Test
    @DisplayName("Return a success response when the drone is registered successfully")
    void registerDroneWhenDroneIsRegisteredThenReturnSuccessResponse() {
        DroneRequest droneRequest = new DroneRequest(
                "ABCD1234",
                DroneModel.LIGHTWEIGHT,
                100.0,
                50.0,
                DroneState.IDLE,
                null);

        Drone drone = new Drone(
                "ABCD1234",
                DroneModel.LIGHTWEIGHT,
                100.0,
                50.0,
                DroneState.IDLE);
        when(droneService.registerDrone(droneRequest)).thenReturn(drone);

        ResponseEntity<ApiResponseDto> response = droneController.registerDrone(droneRequest);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Successful", response.getBody().getMessage());
        assertTrue(response.getBody().isStatus());
        assertEquals(drone, response.getBody().getData());
    }

    @Test
    @DisplayName("Return a bad request response when the drone is not registered")
    void registerDroneWhenDroneIsNotRegisteredThenReturnBadRequestResponse() {
        DroneRequest droneRequest = new DroneRequest(
                "123",
                DroneModel.LIGHTWEIGHT,
                100.0,
                100.0,
                DroneState.IDLE,
                null);
        when(droneService.registerDrone(droneRequest)).thenReturn(null);
        ResponseEntity<ApiResponseDto> response = droneController.registerDrone(droneRequest);
        assertEquals(response.getStatusCode(), OK);
    }

    @Test
    @DisplayName("Return a list of all available drones")
    void getListOfAvailableDrones() {
        when(droneService.listAvailableDronesForLoading()).thenReturn(null);
        ResponseEntity<ApiResponseDto> response = droneController.getAvailableDrone();
        verify(droneService, times(1)).listAvailableDronesForLoading();
    }

    @Test
    @DisplayName("Load the drone with medication when the drone is available")
    void loadADroneWithMedicationWhenDroneIsAvailable() {
        Drone drone = Drone.builder()
                .serialNumber("WIND8060Q20DJ1")
                .model(DroneModel.LIGHTWEIGHT)
                .weightLimit(100.0)
                .batteryCapacity(10.0)
                .state(DroneState.IDLE)
                .build();
        Medication ibuprofen = Medication.builder()
                .name("Ibuprofen")
                .weight( 20.0).code("IBUFEN20")
                .image("src/main/resources/static/images/ibuprofen.png")
                .build();
        Medication amoxicillin = Medication.builder()
                .name("Amoxicillin")
                .weight( 20.0).code("AMOXY50")
                .image("src/main/resources/static/images/amoxicillin.png")
                .build();

        List<Medication> meds = new ArrayList<>();
        meds.add(ibuprofen);
        meds.add(amoxicillin);

        Delivery delivery = Delivery.builder()
                .drone(drone)
                .medications(meds)
                .isDelivered(false)
                .build();
        DeliveryResponseDto deliveryResponseDto = DeliveryResponseDto.fromDelivery(delivery);
        Set<String> medications = new HashSet<>();
        medications.add("IBUFEN20");
        medications.add("AMOXY50");

        LoadingRequest loadingRequest = LoadingRequest.builder()
                .medicationCodes(medications)
                .build();
        String serialNumber = "WIND8060Q20DJ1";
        when(droneService.loadDroneWithMedication(serialNumber, loadingRequest))
                .thenReturn(deliveryResponseDto);

        ResponseEntity<ApiResponseDto> response = droneController.loadADroneWithMedication(serialNumber, loadingRequest);

        assertEquals(OK, response.getStatusCode());
        assertTrue(response.getBody().isStatus());
        assertEquals("Successful", response.getBody().getMessage());
    }



    @Test
    @DisplayName("Check loaded medications and when the drone is loaded")
    void checkLoadedMedicationsWhenTheDroneIsLoaded(){
        Drone drone = Drone.builder()
                .serialNumber("WIND8060Q20DJ1")
                .model(DroneModel.LIGHTWEIGHT)
                .weightLimit(100.0)
                .batteryCapacity(10.0)
                .state(DroneState.IDLE)
                .build();
        LocalDateTime now = LocalDateTime.now();
        Medication ibuprofen = Medication.builder()
                .name("Ibuprofen")
                .weight( 20.0)
                .code("IBUFEN20")
                .image("src/main/resources/static/images/ibuprofen.png")
                .build();
        Medication amoxicillin = Medication.builder()
                .name("Amoxicillin")
                .weight( 20.0)
                .code("AMOXY50")
                .image("src/main/resources/static/images/amoxicillin.png")
                .build();

        List<Medication> meds = new ArrayList<>();
        meds.add(ibuprofen);
        meds.add(amoxicillin);

        Delivery delivery = Delivery.builder()
                .drone(drone)
                .medications(meds)
                .isDelivered(false)
                .build();
        DeliveryResponseDto deliveryResponseDto = DeliveryResponseDto.fromDelivery(delivery);
        when(droneService.checkLoadedMedicationForADrone("WIND8060Q20DJ1")).thenReturn(deliveryResponseDto);
        ResponseEntity<ApiResponseDto> response = droneController.getLoadedMedication("WIND8060Q20DJ1");
        assertEquals(FOUND, response.getStatusCode());
        assertEquals("Retrieved successfully", response.getBody().getMessage());
        assertTrue(response.getBody().isStatus());
        assertEquals(deliveryResponseDto, response.getBody().getData());
    }


    @Test
    @DisplayName("Return the battery level of the drone")
    void checkDroneBatteryLevelShouldReturnTheBatteryLevelOfTheDrone() {
        Drone drone = Drone.builder()
                .serialNumber("SPARK140A0G0CI")
                .model(DroneModel.LIGHTWEIGHT)
                .weightLimit(150.0)
                .batteryCapacity(100.0)
                .state(DroneState.IDLE)
                .build();
        DroneResponseDto droneResponseDto = DroneResponseDto.fromDrone(drone);
        when(droneService.checkDroneBatteryLevel("SPARK140A0G0CI")).thenReturn(droneResponseDto);
        ResponseEntity<ApiResponseDto> response = droneController.checkDroneBatteryLevel("SPARK140A0G0CI");
        assertEquals(FOUND, response.getStatusCode());
        assertEquals(droneResponseDto, response.getBody().getData());

        verify(droneService, times(1)).checkDroneBatteryLevel("SPARK140A0G0CI");
    }
}