package com.chisom.ikemefuna.drone.repository;

import com.chisom.ikemefuna.drone.model.Drone;
import com.chisom.ikemefuna.drone.model.enums.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findDroneBySerialNumber(String serialNumber);
    Optional<Drone> findDroneByState(DroneState state);

    List<Drone> findDronesByState(DroneState state);

    Optional<Drone> findDroneBySerialNumberAndState(String serialNumber, DroneState state);
}
