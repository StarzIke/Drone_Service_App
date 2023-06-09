package com.chisom.ikemefuna.drone.util;

import com.chisom.ikemefuna.drone.exception.*;
import com.chisom.ikemefuna.drone.dto.responseDto.ApiResponseDto;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.*;

public class Responder {

    public static ResponseEntity<ApiResponseDto> success(Object response){
        return new ResponseEntity<>(new ApiResponseDto("Successful", true, response), OK);
    }

    public static ResponseEntity<ApiResponseDto> found (Object response) {
        return new ResponseEntity<>(new ApiResponseDto("Retrieved successfully", true, response), FOUND);
    }

public static ResponseEntity<ApiResponseDto> conflict (Object response) {
    return new ResponseEntity<>(new ApiResponseDto(((DroneAlreadyRegisteredException)response).getMessage(), false), CONFLICT);
}

    public static ResponseEntity<ApiResponseDto> notFound (Object response) {
        return new ResponseEntity<>(new ApiResponseDto(((NoAvailableDroneException)response).getMessage(), false), NOT_FOUND);
    }

    public static ResponseEntity<ApiResponseDto> overWeight (Object response) {
        return new ResponseEntity<>(new ApiResponseDto<>(((WeightLimitExceededException)response).getMessage(), false), FORBIDDEN);
    }

    public static ResponseEntity<ApiResponseDto> lowBattery (Object response) {
        return new ResponseEntity<>(new ApiResponseDto<>(((DroneBatteryLowException)response).getMessage(), false), FORBIDDEN);
    }

    public static ResponseEntity<ApiResponseDto> badRequest (Object response) {
        return new ResponseEntity<>(new ApiResponseDto<>(((InvalidLoadRequestException)response).getMessage(), false), BAD_REQUEST);
    }
}
