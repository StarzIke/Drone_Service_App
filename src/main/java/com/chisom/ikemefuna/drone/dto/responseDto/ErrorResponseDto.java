package com.chisom.ikemefuna.drone.dto.responseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {

    private String message;
    private LocalDateTime timeStamp = LocalDateTime.now();
}
