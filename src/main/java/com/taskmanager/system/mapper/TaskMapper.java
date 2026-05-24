package com.taskmanager.system.mapper;

import com.taskmanager.system.dto.response.TaskResponseDTO;
import com.taskmanager.system.entity.Task;

public class TaskMapper {
    public static TaskResponseDTO toDTO(Task task){
        TaskResponseDTO dto = new TaskResponseDTO();

        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus().name());
        dto.setCreatedBy(task.getCreatedBy().getEmail());
        dto.setAssignedTo(task.getAssignedTo().getEmail());
        return dto;
    }
}
