package com.chisom.ikemefuna.drone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "deliveries")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Delivery extends BaseModel {
    @OneToOne(cascade = CascadeType.ALL)
    private Drone drone;
    @OneToMany
    private List<Medication> medications;
    private Boolean isDelivered = false;
}
