package com.chisom.ikemefuna.drone.exception;


public class WeightLimitExceededException extends RuntimeException {
    public WeightLimitExceededException(String message) {
        super(message);
    }
}
