package com.chisom.ikemefuna.drone.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
public class MedicationRequest {

    @NotBlank(message = "Medication name is required")
    @Pattern(regexp = "^[\\w-]*$",
            message = "Medication name should only contain letters, numbers, underscore and hyphen")
    private String name;

    @NotNull(message = "Medication weight is required")
    private Double weight;

    @NotBlank(message = "Medication code is required")
    @Pattern(regexp = "^[A-Z0-9_]*$",
            message = "Medication code should only contain UPPERCASE letters, underscore and numbers")
    private String code;

    @NotBlank(message = "Picture of medication case is required")
    private String image;
}
