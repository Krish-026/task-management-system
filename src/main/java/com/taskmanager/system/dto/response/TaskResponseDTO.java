package com.taskmanager.system.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String createdBy;
    private String assignedTo;
}
