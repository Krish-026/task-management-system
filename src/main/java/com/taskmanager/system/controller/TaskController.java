package com.taskmanager.system.controller;

import com.taskmanager.system.dto.request.TaskRequestDTO;
import com.taskmanager.system.dto.response.CustomApiResponse;
import com.taskmanager.system.dto.response.TaskResponseDTO;
import com.taskmanager.system.entity.Task;
import com.taskmanager.system.enums.TaskStatus;
import com.taskmanager.system.mapper.TaskMapper;
import com.taskmanager.system.projection.TaskSummaryProjection;
import com.taskmanager.system.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Tag(name = "Task APIs", description = "Task management operations")
@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Create a new task", description = "Creates a task and assigns it to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task Created Successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Input Data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT missing or invalid")
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping
    public ResponseEntity<CustomApiResponse<TaskResponseDTO>> createTask(
            @RequestBody TaskRequestDTO request) {
        // 1. Call service
        Task task = taskService.createTask(request.getTitle(), request.getDescription(), request.getAssignedToUserId());
        // 2. Map to entity to DTO
        TaskResponseDTO dto = TaskMapper.toDTO(task);
        CustomApiResponse<TaskResponseDTO> response = CustomApiResponse.<TaskResponseDTO>builder()
                .success(true)
                .message("Task created successfully")
                .data(dto)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<CustomApiResponse<List<TaskResponseDTO>>> getMyTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
            ) {

//        List<Task> tasks = taskService.getMyTasks();
//
//        List<TaskResponseDTO> dtos = tasks.stream()
//                .map(TaskMapper::toDTO)
//                .toList();
//        CustomApiResponse<List<TaskResponseDTO>> response = CustomApiResponse.<List<TaskResponseDTO>>builder()
//                .success(true)
//                .message("Fetched user tasks")
//                .data(dtos)
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        return ResponseEntity.ok(response);

        Page<Task> taskPage = taskService.getTasks(status, page, size);
        List<TaskResponseDTO> dtos = taskPage.getContent()
                .stream()
                .map(TaskMapper::toDTO)
                .toList();

        CustomApiResponse<List<TaskResponseDTO>> response = new CustomApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Task Fetched successfully");
        response.setData(dtos);
        response.setTimestamp(LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<CustomApiResponse<Map<String, Object>>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String title
            ) {
        Page<Task> taskPage = taskService.getAllTasks(page, size, sortBy, direction, status, title);
        List<TaskResponseDTO> dtos = taskPage.getContent()
                .stream()
                .map(TaskMapper::toDTO)
                .toList();
        /*
        CustomApiResponse<List<TaskResponseDTO>> response = CustomApiResponse.<List<TaskResponseDTO>>builder()
                .success(true)
                .message("Fetched user tasks")
                .data(dtos)
                .timestamp(LocalDateTime.now())
                .build();
        */
        Map<String, Object> result = new HashMap<>();
        result.put("tasks", dtos);
        result.put("currentPage", taskPage.getNumber());
        result.put("totalPages", taskPage.getTotalPages());
        result.put("totalElements", taskPage.getTotalElements());

        CustomApiResponse<Map<String, Object>> response = CustomApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Task fetched successfully")
                .data(result)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or @taskSecurity.isOwner(#taskId) or @taskSecurity.isAssigned(#taskId)")
    @PutMapping("/{taskId}")
    public ResponseEntity<CustomApiResponse<TaskResponseDTO>> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskRequestDTO request
    ) {
        Task updatedTask = taskService.updateTask(taskId, request.getTitle(), request.getDescription(), request.getStatus());
        TaskResponseDTO dto = TaskMapper.toDTO(updatedTask);
        CustomApiResponse<TaskResponseDTO> response = CustomApiResponse.<TaskResponseDTO>builder()
                .success(true)
                .message("Task updated successfully")
                .data(dto)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<CustomApiResponse<String>> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        CustomApiResponse<String> response = CustomApiResponse.<String>builder()
                .success(true)
                .message("Task delete succesfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<CustomApiResponse<List<TaskResponseDTO>>> getTasksByStatus(@PathVariable TaskStatus status){
        List<Task> tasks = taskService.getTasksByStatus(status);
        List<TaskResponseDTO> dtos = tasks.stream()
                .map(TaskMapper::toDTO)
                .toList();

        CustomApiResponse<List<TaskResponseDTO>> response = CustomApiResponse.<List<TaskResponseDTO>>builder()
                .success(true)
                .message("Tasks fetched successfully")
                .data(dtos)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<CustomApiResponse<Page<TaskSummaryProjection>>> getTaskSummeries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Page<TaskSummaryProjection> tasks = taskService.getTaskSummaries(page, size);

        CustomApiResponse<Page<TaskSummaryProjection>> response = CustomApiResponse.<Page<TaskSummaryProjection>>builder()
                .success(true)
                .message("Task summaries fetched")
                .data(tasks)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/optimized")
    public ResponseEntity<CustomApiResponse<List<TaskResponseDTO>>> getAllTasksOptimized(){
        List<Task> tasks = taskService.getAllTasksOptimized();

        List<TaskResponseDTO> dtos = tasks.stream()
                .map(TaskMapper::toDTO)
                .collect(Collectors.toList());

        CustomApiResponse<List<TaskResponseDTO>> response = CustomApiResponse.<List<TaskResponseDTO>>builder()
                .success(true)
                .message("Fetched all tasks with user details efficiently")
                .data(dtos)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<CustomApiResponse<TaskResponseDTO>> getTaskById(@PathVariable Long taskId){
        TaskResponseDTO dto = taskService.getTaskById(taskId);

        CustomApiResponse<TaskResponseDTO> response = CustomApiResponse.<TaskResponseDTO>builder()
                .success(true)
                .message("Task fetched successfully")
                .data(dto)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}