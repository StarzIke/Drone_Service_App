package com.chisom.ikemefuna.drone.exception;


public class DroneAlreadyRegisteredException extends RuntimeException {
    public DroneAlreadyRegisteredException(String message) {
        super(message);
    }
}
