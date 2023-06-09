package com.chisom.ikemefuna.drone.service.implementation;

import com.chisom.ikemefuna.drone.exception.*;

import com.chisom.ikemefuna.drone.model.Delivery;
import com.chisom.ikemefuna.drone.model.Drone;
import com.chisom.ikemefuna.drone.model.Medication;
import com.chisom.ikemefuna.drone.model.enums.DroneModel;
import com.chisom.ikemefuna.drone.model.enums.DroneState;
import com.chisom.ikemefuna.drone.dto.requestDto.DroneRequest;
import com.chisom.ikemefuna.drone.dto.requestDto.LoadingRequest;
import com.chisom.ikemefuna.drone.dto.responseDto.DeliveryResponseDto;
import com.chisom.ikemefuna.drone.dto.responseDto.DroneResponseDto;
import com.chisom.ikemefuna.drone.repository.DeliveryRepository;
import com.chisom.ikemefuna.drone.repository.DroneRepository;
import com.chisom.ikemefuna.drone.repository.MedicationRepository;
import com.chisom.ikemefuna.drone.service.DroneService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DroneServiceImplementation implements DroneService {

    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final DeliveryRepository deliveryRepository;

    public  void addDelivery(){
        Delivery delivery = new Delivery();
        delivery.setDrone(new Drone("WIND8060Q20DJ1", DroneModel.CRUISERWEIGHT,
                250.0, 100.0	, DroneState.DELIVERED));
        delivery.setId(1L);
        delivery.setMedications(new ArrayList<>());
        delivery.setUpdatedAt(LocalDateTime.now());
        delivery.setCreatedAt(LocalDateTime.of(6, Month.JUNE, 2,0,23));
        deliveryRepository.saveAndFlush(delivery);
    }

    @Override
    public Drone registerDrone(DroneRequest droneRequest) {
        Optional<Drone> registeredDrone =
                droneRepository.findDroneBySerialNumber(droneRequest.getSerialNumber());
        if (registeredDrone.isEmpty()) {
            Drone drone = Drone.builder()
                    .model(droneRequest.getModel())
                    .weightLimit(droneRequest.getWeightLimit())
                    .batteryCapacity(droneRequest.getBatteryCapacity())
                    .serialNumber(droneRequest.getSerialNumber())
                    .state(DroneState.IDLE)
                    .build();
            return droneRepository.save(drone);
        } else {
            throw new DroneAlreadyRegisteredException("Drone with serialNumber: "
                    + droneRequest.getSerialNumber()
                    + " is already registered");
        }
    }

    @Override
    public List<Drone> listAvailableDronesForLoading() {
        return droneRepository.findDronesByState(DroneState.IDLE)
                .stream()
                .filter(drone -> drone.getBatteryCapacity() >= 25f)
                .collect(Collectors.toList());
    }


    @Override
    public DeliveryResponseDto loadDroneWithMedication(String serialNumber, LoadingRequest medicationCodes) {

        Drone drone = droneRepository.findDroneBySerialNumberAndState(serialNumber, DroneState.IDLE).orElse(null);
        if (drone.getBatteryCapacity() < 25f) {
            throw new DroneBatteryLowException("Drone with serialNumber: "
                    + serialNumber
                    + " is not available for loading");
        }
        List<Medication> medicationList = medicationCodes.getMedicationCodes().stream().map(a-> Medication.builder().code(a).build()).collect(Collectors.toList());
        medicationList = medicationRepository.findAllByCodeIn(medicationCodes.getMedicationCodes());

        if (medicationList.size() == 0){
            throw new InvalidLoadRequestException("No medication with the code(s) provided");
        }
        double sum = medicationList.stream().mapToDouble(Medication::getWeight).sum();
        assert drone != null;
        if(sum <= drone.getWeightLimit()){
            drone.setState(DroneState.LOADED);
            droneRepository.save(drone);
            Delivery delivery = Delivery.builder()
                    .drone(drone)
                    .medications(medicationList)
                    .isDelivered(false)
                    .build();
            return DeliveryResponseDto.fromDelivery(deliveryRepository.save(delivery));
        }else{
            throw new WeightLimitExceededException("The total weight of the medication(s) exceeds the drone's weight limit");
        }
    }

    @Override
    public DroneResponseDto checkDroneBatteryLevel(String serialNumber) {
        Drone drone = droneRepository.findDroneBySerialNumber(serialNumber)
                .orElseThrow(() -> new NoAvailableDroneException("Drone with input serialNumber: "
                        + serialNumber
                        + " not found"));

        return DroneResponseDto.builder()
                .serialNumber(drone.getSerialNumber())
                .batteryLevel(drone.getBatteryCapacity())
                .build();
    }

    @Override
    public DeliveryResponseDto checkLoadedMedicationForADrone(String serialNumber) {
        Drone drone = droneRepository.findDroneBySerialNumberAndState(serialNumber, DroneState.LOADED)
                .orElseThrow(() -> new NoAvailableDroneException
                        ("Drone with serial number: " + serialNumber + " is not loaded with medication"));
          return DeliveryResponseDto.fromDelivery(deliveryRepository.findDeliveryByDroneAndIsDelivered(drone, false));
    }
}