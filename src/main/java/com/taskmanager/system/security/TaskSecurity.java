package com.taskmanager.system.security;

import com.taskmanager.system.entity.Task;
import com.taskmanager.system.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskSecurity {
    private final TaskRepository taskRepository;

    public boolean isOwner(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return task.getCreatedBy().getEmail().equals(currentUserEmail);
    }

    public boolean isAssigned(Long taskId){
        Task task = taskRepository.findById(taskId).orElseThrow();

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        return task.getAssignedTo().getEmail().equals(currentUserEmail);
    }
}
