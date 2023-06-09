package com.chisom.ikemefuna.drone.controller;

import com.chisom.ikemefuna.drone.dto.requestDto.DroneRequest;
import com.chisom.ikemefuna.drone.dto.requestDto.LoadingRequest;
import com.chisom.ikemefuna.drone.dto.responseDto.ApiResponseDto;
import com.chisom.ikemefuna.drone.dto.responseDto.DeliveryResponseDto;
import com.chisom.ikemefuna.drone.service.DroneService;
import com.chisom.ikemefuna.drone.service.implementation.DroneServiceImplementation;
import com.chisom.ikemefuna.drone.util.Responder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/drone")
public class DroneController {

    private final DroneService droneService;
    private final DroneServiceImplementation impl;
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> registerDrone(@RequestBody @Valid DroneRequest droneRequest){
        impl.addDelivery();
        return Responder.success(droneService.registerDrone(droneRequest));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponseDto> getAvailableDrone(){
        return Responder.found(droneService.listAvailableDronesForLoading());
    }

    @PostMapping("/load-medication/{drone-serial}")
    public ResponseEntity<ApiResponseDto> loadADroneWithMedication(
            @PathVariable("drone-serial") String serialNumber ,
            @RequestBody LoadingRequest medicationCodes) {
        return Responder.success(droneService.loadDroneWithMedication(serialNumber, medicationCodes));
    }

    @GetMapping("/check-battery/{drone-serial}")
    public ResponseEntity<ApiResponseDto> checkDroneBatteryLevel(@PathVariable("drone-serial") String serialNumber) {
        return Responder.found(droneService.checkDroneBatteryLevel(serialNumber));
    }

    @GetMapping("/check-loaded-medication/{drone-serial}")
    public ResponseEntity<ApiResponseDto> getLoadedMedication(@PathVariable("drone-serial") String serialNumber) {

        ApiResponseDto responseDto = new ApiResponseDto();
        try {
            DeliveryResponseDto droneDto = droneService.checkLoadedMedicationForADrone(serialNumber);
            responseDto.setData(droneDto);
            return ResponseEntity.ok(responseDto);
        }catch (Exception e){
            responseDto.setMessage("Drone with loaded medication not found");
            return  ResponseEntity.ok(responseDto);
        }
    }
}
