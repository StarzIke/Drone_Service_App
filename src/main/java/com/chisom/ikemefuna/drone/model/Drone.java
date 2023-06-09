package com.chisom.ikemefuna.drone.model;

import com.chisom.ikemefuna.drone.model.enums.DroneModel;
import com.chisom.ikemefuna.drone.model.enums.DroneState;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "drones")
@NoArgsConstructor
@Data
public class Drone extends BaseModel {

    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private DroneModel model;

    private Double weightLimit;

    private Double batteryCapacity;

    @Enumerated(EnumType.STRING)
    private DroneState state;

}
