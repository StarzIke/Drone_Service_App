package com.chisom.ikemefuna.drone.dto.responseDto;

import com.chisom.ikemefuna.drone.model.Drone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DroneResponseDto {
    private String serialNumber;
    private Double batteryLevel;

    public static DroneResponseDto fromDrone(Drone drone) {
        return DroneResponseDto.builder()
                .serialNumber(drone.getSerialNumber())
                .batteryLevel(drone.getBatteryCapacity())
                .build();
    }
}
