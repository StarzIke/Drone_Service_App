package com.chisom.ikemefuna.drone.controller;

import com.chisom.ikemefuna.drone.exception.*;
import com.chisom.ikemefuna.drone.util.Responder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(DroneAlreadyRegisteredException.class)
    public ResponseEntity<?> droneAlreadyRegisteredException(DroneAlreadyRegisteredException exception) {
        return Responder.conflict(exception);
    }

    @ExceptionHandler(DroneBatteryLowException.class)
    public ResponseEntity<?> droneBatteryLowException(DroneBatteryLowException exception) {
        return Responder.lowBattery(exception);
    }

    @ExceptionHandler(WeightLimitExceededException.class)
    public ResponseEntity<?> maximumWeightExceededException(WeightLimitExceededException exception) {
        return Responder.overWeight(exception);
    }

    @ExceptionHandler(MedicationAlreadyExistException.class)
    public ResponseEntity<?> medicationAlreadyExistException(MedicationAlreadyExistException exception) {
        return Responder.conflict(exception);
    }

    @ExceptionHandler(NoAvailableDroneException.class)
    public ResponseEntity<?> noAvailableDroneException(NoAvailableDroneException exception) {
        return Responder.notFound(exception);
    }

    @ExceptionHandler(InvalidLoadRequestException.class)
    public ResponseEntity<?> invalidLoadRequestException(InvalidLoadRequestException exception) {
        return Responder.badRequest(exception);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }
}
