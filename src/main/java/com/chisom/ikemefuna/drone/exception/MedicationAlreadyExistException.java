package com.chisom.ikemefuna.drone.exception;


public class MedicationAlreadyExistException extends RuntimeException {
    public MedicationAlreadyExistException(String message) {
        super (message);
    }
}
