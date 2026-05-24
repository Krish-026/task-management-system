package com.taskmanager.system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomApiResponse<T> {
    private String message;
    private T data;
    private boolean success;
    private LocalDateTime timestamp;
}
