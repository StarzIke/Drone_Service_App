package com.chisom.ikemefuna.drone.dto.responseDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto<T>{
    private String message;

    private boolean status;

    private T data;

    public ApiResponseDto(String message, boolean status) {
        this.message = message;
        this.status = status;
    }
}
