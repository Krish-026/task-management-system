package com.taskmanager.system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {

    @Schema(description = "Title of the task", example = "Fix login bug")
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "Task description", example = "Fix JWT issue")
    @NotBlank(message = "Description is required")
    private String description;

    @Schema(description = "Task assigned to user", example = "Assigned to user")
    private Long assignedToUserId;

    @Schema(description = "Task status", example = "TODO, IN_PROGRESS, DONE")
    private String status;


}
