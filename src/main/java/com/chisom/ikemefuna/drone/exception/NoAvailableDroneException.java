package com.chisom.ikemefuna.drone.exception;


public class NoAvailableDroneException extends RuntimeException{
    public NoAvailableDroneException(String message) {
        super(message);
    }
}
