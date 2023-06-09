package com.chisom.ikemefuna.drone.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.chisom.ikemefuna.drone.model.Delivery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class DeliveryResponseDto {
    private DroneResponseDto drone;
    private List<MedicationResponseDto> medications;
    @CreationTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss a")
    private LocalDateTime createAt;

    public static DeliveryResponseDto fromDelivery(Delivery delivery) {
        return DeliveryResponseDto.builder()
                .drone(DroneResponseDto.fromDrone(delivery.getDrone()))
                .medications(delivery.getMedications().stream().map(MedicationResponseDto::fromMedication).collect(Collectors.toList()))
                .createAt(delivery.getCreatedAt())
                .build();
    }
}
