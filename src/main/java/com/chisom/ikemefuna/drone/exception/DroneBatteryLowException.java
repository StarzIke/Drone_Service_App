package com.chisom.ikemefuna.drone.exception;


public class DroneBatteryLowException extends RuntimeException {
    public DroneBatteryLowException(String message)  {
        super(message);
    }
}


