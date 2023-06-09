package com.chisom.ikemefuna.drone.dto.requestDto;

import com.chisom.ikemefuna.drone.model.Medication;
import com.chisom.ikemefuna.drone.model.enums.DroneModel;
import com.chisom.ikemefuna.drone.model.enums.DroneState;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DroneRequest {

    @NotBlank(message = "Drone serialNumber is required")
    @Size(max = 100, message = "Serial number is not more than 100 characters")
    private String serialNumber;

    @NotNull(message = "Drone model is required")
    private DroneModel model;

    @NotNull(message = " Drone weight limit is required")
    @DecimalMax(value = "500", message =" Drone weight limit is {value} grams maximum")
    private Double weightLimit;

    @NotNull(message = "Drone battery capacity is required")
    @DecimalMax(value = "100")
    private Double batteryCapacity;

    private DroneState state;

    private List<Medication> medication;
}
