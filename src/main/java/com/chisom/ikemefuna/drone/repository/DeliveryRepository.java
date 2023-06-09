package com.chisom.ikemefuna.drone.repository;

import com.chisom.ikemefuna.drone.model.Delivery;
import com.chisom.ikemefuna.drone.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Delivery findDeliveryByDroneAndIsDelivered(Drone drone, Boolean isDelivered);
}
