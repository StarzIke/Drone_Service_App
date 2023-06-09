package com.chisom.ikemefuna.drone.repository;

import com.chisom.ikemefuna.drone.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findAllByCodeIn(Set<String> codes);
}
