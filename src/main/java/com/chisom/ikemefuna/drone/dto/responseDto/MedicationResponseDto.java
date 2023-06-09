package com.chisom.ikemefuna.drone.dto.responseDto;

import com.chisom.ikemefuna.drone.model.Medication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicationResponseDto {
    private String name;
    private double weight;
    private String code;
    private String image;

    public static MedicationResponseDto fromMedication(Medication medication) {
        return MedicationResponseDto.builder()
                .name(medication.getName())
                .weight(medication.getWeight())
                .code(medication.getCode())
                .image(medication.getImage())
                .build();
    }
}
