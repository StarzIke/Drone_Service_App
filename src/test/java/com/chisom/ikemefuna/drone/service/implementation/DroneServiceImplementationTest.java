package com.chisom.ikemefuna.drone.service.implementation;

import com.chisom.ikemefuna.drone.exception.*;
import com.chisom.ikemefuna.drone.model.Delivery;
import com.chisom.ikemefuna.drone.model.enums.DroneModel;
import com.chisom.ikemefuna.drone.model.enums.DroneState;
import com.chisom.ikemefuna.drone.dto.requestDto.DroneRequest;
import com.chisom.ikemefuna.drone.dto.requestDto.LoadingRequest;
import com.chisom.ikemefuna.drone.dto.responseDto.DeliveryResponseDto;
import com.chisom.ikemefuna.drone.dto.responseDto.DroneResponseDto;
import com.chisom.ikemefuna.drone.model.Drone;
import com.chisom.ikemefuna.drone.model.Medication;
import com.chisom.ikemefuna.drone.repository.DeliveryRepository;
import com.chisom.ikemefuna.drone.repository.DroneRepository;
import com.chisom.ikemefuna.drone.repository.MedicationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DroneServiceImplementationTest {

    @Mock
    private DroneRepository droneRepository;
    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private DroneServiceImplementation droneService;

    @Test
    @DisplayName("Save the drone when the serial number is not taken")
    void registerDroneWhenSerialNumberIsNotTaken() {
        DroneRequest droneRequest =
                DroneRequest.builder()
                        .serialNumber("123456789")
                        .model(DroneModel.LIGHTWEIGHT)
                        .weightLimit(100d)
                        .batteryCapacity(100d)
                        .build();
        Drone  droneReturn = Drone.builder().serialNumber("123456789")
                .model(DroneModel.LIGHTWEIGHT)
                .weightLimit(100.0)
                .batteryCapacity(100.0)
                .state(DroneState.IDLE)
                .build();
        when(droneRepository.findDroneBySerialNumber(droneRequest.getSerialNumber()))
                .thenReturn(Optional.empty());
        when(droneRepository.save(any(Drone.class))).thenReturn(droneReturn);
        Drone drone = droneService.registerDrone(droneRequest);

        assertNotNull(drone);
        assertEquals(droneRequest.getSerialNumber(), drone.getSerialNumber());
        assertEquals(droneRequest.getModel(), drone.getModel());
        assertEquals(droneRequest.getWeightLimit(), drone.getWeightLimit());
        assertEquals(droneRequest.getBatteryCapacity(), drone.getBatteryCapacity());
        assertEquals(DroneState.IDLE, drone.getState());

        verify(droneRepository, times(1)).findDroneBySerialNumber(droneRequest.getSerialNumber());
        verify(droneRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Throw an exception when the serial number is already taken")
    void registerDroneWhenSerialNumberIsAlreadyTakenThenThrowException() {
        DroneRequest droneRequest =
                DroneRequest.builder()
                        .serialNumber("123456789")
                        .model(DroneModel.LIGHTWEIGHT)
                        .weightLimit(100d)
                        .batteryCapacity(100d)
                        .build();

        when(droneRepository.findDroneBySerialNumber(droneRequest.getSerialNumber()))
                .thenReturn(Optional.of(Drone.builder().build()));

        assertThrows(
                DroneAlreadyRegisteredException.class,
                () -> droneService.registerDrone(droneRequest));

        verify(droneRepository, times(1)).findDroneBySerialNumber(droneRequest.getSerialNumber());
    }

    @Test
    @DisplayName("Return a list of drones when there is at least one available drone for loading")
    void listAvailableDronesForLoadingWhenThereIsAtLeastOneAvailableDroneThenReturnAListOfDrones() {
        Drone drone =
                Drone.builder()
                        .model(DroneModel.LIGHTWEIGHT)
                        .weightLimit(100d)
                        .batteryCapacity(100d)
                        .serialNumber("123456789")
                        .state(DroneState.IDLE)
                        .build();

        when(droneRepository.findDronesByState(DroneState.IDLE)).thenReturn(List.of(drone));

        assertEquals(1, droneService.listAvailableDronesForLoading().size());
    }



    @Test
    @DisplayName("Return an empty list when there is no available drone for loading")
    void listAvailableDronesForLoadingWhenThereIsNoAvailableDroneThenReturnEmptyList() {
        when(droneRepository.findDronesByState(DroneState.IDLE)).thenReturn(List.of());

        assertTrue(droneService.listAvailableDronesForLoading().isEmpty());
    }



    @Test
    @DisplayName("Check for exception and successful loading of a drone")
    void checkLoadedMedicationForADrone() {
        when(droneRepository.findDroneBySerialNumberAndState("12345", DroneState.LOADED))
                .thenReturn(Optional.empty());
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
                .weight( 20.0).code("IBUFEN20")
                .image("src/main/resources/static/images/ibuprofen.png")
                .build();
        Medication amoxicillin = Medication.builder()
                .name("Amoxicillin")
                .weight( 20.0).code("AMOXY50")
                .image("src/main/resources/static/images/amoxicillin.png")
                .build();
        List<Medication> medications = new ArrayList<>();
        medications.add(ibuprofen);
        medications.add(amoxicillin);
        Delivery delivery = Delivery.builder()
                .drone(drone)
                .medications(medications)
                .isDelivered(false)
                .build();
        DeliveryResponseDto deliveryResponseDto = DeliveryResponseDto.fromDelivery(delivery);
        when(droneRepository.findDroneBySerialNumberAndState("WIND8060Q20DJ1", DroneState.LOADED))
                .thenReturn(Optional.of(drone));
        when(deliveryRepository.findDeliveryByDroneAndIsDelivered(drone, false))
                .thenReturn(delivery);

        //Assertions
        assertThrows(
                NoAvailableDroneException.class,
                () -> droneService.checkLoadedMedicationForADrone("12345"));
        Assertions.assertEquals(deliveryResponseDto, droneService.checkLoadedMedicationForADrone("WIND8060Q20DJ1"));
    }



    @Test
    @DisplayName("Check load drone with medication and test for three exceptions and successful loading of a drone")
    void loadDroneWithMedication() {
        Drone lowBatteryDrone =
                Drone.builder()
                        .model(DroneModel.LIGHTWEIGHT)
                        .weightLimit(100.0)
                        .batteryCapacity(12.0)
                        .serialNumber("123456789")
                        .state(DroneState.IDLE)
                        .build();
        Drone highBatteryDrone =
                Drone.builder()
                        .model(DroneModel.LIGHTWEIGHT)
                        .weightLimit(100.0)
                        .batteryCapacity(50.0)
                        .serialNumber("GHJCWDKJ32")
                        .state(DroneState.IDLE)
                        .build();
        Drone lowWeightDrone =
                Drone.builder()
                        .model(DroneModel.LIGHTWEIGHT)
                        .weightLimit(20.0)
                        .batteryCapacity(50.0)
                        .serialNumber("LWEIGHT000")
                        .state(DroneState.IDLE)
                        .build();
        Set<String> fakeMedicationCodes = Set.of("IBUFEN10", "AMOXY20");
        Set<String> realMedicationCodes = Set.of("IBUFEN20", "AMOXY50");
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
                .drone(highBatteryDrone)
                .medications(meds)
                .isDelivered(false)
                .build();
        DeliveryResponseDto deliveryResponseDto = DeliveryResponseDto.fromDelivery(delivery);
        //Mocks requests
        when(droneRepository.findDroneBySerialNumberAndState("123456789", DroneState.IDLE))
                .thenReturn(Optional.of(lowBatteryDrone));
        when(droneRepository.findDroneBySerialNumberAndState("GHJCWDKJ32", DroneState.IDLE))
                .thenReturn(Optional .of(highBatteryDrone));
        when(droneRepository.findDroneBySerialNumberAndState("LWEIGHT000", DroneState.IDLE))
                .thenReturn(Optional.of(lowWeightDrone));
        when(medicationRepository.findAllByCodeIn(realMedicationCodes)).thenReturn(meds);
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);
        when(medicationRepository.findAllByCodeIn(fakeMedicationCodes)).thenReturn(new ArrayList<>());

        //Assertions
        assertThrows(
                DroneBatteryLowException.class,
                () -> droneService.loadDroneWithMedication("123456789", new LoadingRequest(fakeMedicationCodes)));

        assertThrows(
                InvalidLoadRequestException.class,
                () -> droneService.loadDroneWithMedication("GHJCWDKJ32", new LoadingRequest(fakeMedicationCodes)));

        assertThrows(
                WeightLimitExceededException.class,
                () -> droneService.loadDroneWithMedication("LWEIGHT000", new LoadingRequest(realMedicationCodes)));

        Assertions.assertEquals(deliveryResponseDto, droneService.loadDroneWithMedication("GHJCWDKJ32", new LoadingRequest(realMedicationCodes)));
    }


    @Test
    @DisplayName("Check for drone battery level and check exception for drone not found")
    void checkDroneBatteryLevel() {
        when(droneRepository.findDroneBySerialNumber("12345")).thenReturn(Optional.empty());
        assertThrows(
                NoAvailableDroneException.class,
                () -> droneService.checkDroneBatteryLevel("12345"));
        Drone drone = Drone.builder().serialNumber("GHJCWDKJ32").weightLimit(100.0).batteryCapacity(100.0).state(DroneState.IDLE).build();
        DroneResponseDto droneResponseDto = DroneResponseDto.fromDrone(drone);
        when(droneRepository.findDroneBySerialNumber("GHJCWDKJ32")).thenReturn(Optional.of(drone));
        Assertions.assertEquals(droneResponseDto, droneService.checkDroneBatteryLevel("GHJCWDKJ32"));
    }
}