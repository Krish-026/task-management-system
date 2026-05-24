package com.taskmanager.system.service;

import com.taskmanager.system.dto.response.TaskResponseDTO;
import com.taskmanager.system.entity.Task;
import com.taskmanager.system.enums.TaskStatus;
import com.taskmanager.system.projection.TaskSummaryProjection;
import org.springframework.data.domain.Page;

import java.util.List;


public interface TaskService {
    Task createTask(String title, String description, Long assignedToUserId);
    List<Task> getMyTasks();
    Page<Task> getAllTasks(int page, int size, String sortBy, String direction, TaskStatus status, String title); // admin
    List<Task> getTasksByStatus(TaskStatus status);
    Task updateTask(Long taskId, String title, String description, String status);
    void deleteTask(Long taskId);
    Page<Task> getTasks(TaskStatus status, int page, int size);
    Page<TaskSummaryProjection> getTaskSummaries(int page, int size);
    List<Task> getAllTasksOptimized();
    TaskResponseDTO getTaskById(Long taskId);
}