package com.chisom.ikemefuna.drone;

import com.chisom.ikemefuna.drone.model.Delivery;
import com.chisom.ikemefuna.drone.model.Drone;
import com.chisom.ikemefuna.drone.model.Medication;
import com.chisom.ikemefuna.drone.model.enums.DroneModel;
import com.chisom.ikemefuna.drone.model.enums.DroneState;
import com.chisom.ikemefuna.drone.repository.DeliveryRepository;
import com.chisom.ikemefuna.drone.service.implementation.DroneServiceImplementation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.chisom.ikemefuna.drone.repository.DroneRepository;
import com.chisom.ikemefuna.drone.repository.MedicationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class DroneApplication {
    public static void main(String[] args) {
        SpringApplication.run(DroneApplication.class, args);
    }
    @Bean
    CommandLineRunner runner(DroneRepository droneRepository, MedicationRepository medicationRepository,
                             DeliveryRepository deliveryRepository) {
        return args -> {
            // read json and write to db
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Drone>> typeReference = new TypeReference<>() {};
            TypeReference<List<Medication>> medicationTypeReference = new TypeReference<>(){};
            InputStream droneInputStream = TypeReference.class.getResourceAsStream("/drone.json");
            InputStream medicationInputStream = TypeReference.class.getResourceAsStream("/medication.json");

            Delivery delivery  = new Delivery();

            try {
                List<Drone> drones = mapper.readValue(droneInputStream, typeReference);
                List<Medication> medications = mapper.readValue(medicationInputStream, medicationTypeReference);
                droneRepository.saveAll(drones);
                medicationRepository.saveAll(medications);
            } catch (IOException e) {
                throw new RuntimeException("Error occurred while reading from file");
            }
        };
    }
}
