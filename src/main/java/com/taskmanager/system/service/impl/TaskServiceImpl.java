package com.taskmanager.system.service.impl;

import com.taskmanager.system.dto.response.TaskResponseDTO;
import com.taskmanager.system.entity.Task;
import com.taskmanager.system.entity.User;
import com.taskmanager.system.enums.Role;
import com.taskmanager.system.enums.TaskStatus;
import com.taskmanager.system.exception.BadRequestException;
import com.taskmanager.system.exception.ResourceNotFoundException;
import com.taskmanager.system.mapper.TaskMapper;
import com.taskmanager.system.projection.TaskSummaryProjection;
import com.taskmanager.system.repository.TaskRepository;
import com.taskmanager.system.repository.UserRepository;
import com.taskmanager.system.service.TaskService;
import com.taskmanager.system.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public Task createTask(String title, String description, Long assignedToUserId) {
        // Get Current Logged-in user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User createdBy = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        User assignedTo = userRepository.findById(assignedToUserId).orElseThrow(()-> new ResourceNotFoundException("Assinged user not found"));

        Task task = Task.builder()
                .title(title)
                .description(description)
                .status(TaskStatus.TODO)
                .createdBy(createdBy)
                .assignedTo(assignedTo)
                .build();
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getMyTasks() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));

        return taskRepository.findByAssignedTo(user);
    }

    @Override
    public Page<Task> getAllTasks(int page, int size, String sortBy, String direction, TaskStatus status, String title) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Task> specification = Specification
                .where(TaskSpecification.hasStatus(status))
                .and(TaskSpecification.titleContains(title));
        return taskRepository.findAll(specification, pageable);
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus status){
        return taskRepository.findByStatus(status);
    }

    @Override
    public Task updateTask(Long taskId, String title, String description, String status) {
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new ResourceNotFoundException("Task not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Authorization check
        if(!task.getCreatedBy().getId().equals(currentUser.getId())){
            throw new ResourceNotFoundException("You are not allowed to update this task");
        }

        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskStatus.valueOf(status));

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        // Check Task is present or not
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Current user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // fetch user detail
        User currentUser = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User Not found"));

        // only creator or admin can delete
        if(!task.getCreatedBy().getId().equals(currentUser.getId()) && currentUser.getRole() != Role.ADMIN){
            throw new BadRequestException("Not authorized to delete task");
        }

        // Delete task;
        taskRepository.delete(task);
    }

    @Override
    public Page<Task> getTasks(TaskStatus status, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        if(status != null){
            return taskRepository.findByStatus(status, pageable);
        }

        return taskRepository.findAll(pageable);
    }

    @Override
    public Page<TaskSummaryProjection> getTaskSummaries(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return taskRepository.findAllProjectedBy(pageable);
    }

    @Override
    public List<Task> getAllTasksOptimized(){
        return taskRepository.findAllWithUsers();
    }

    @Override
    @Cacheable(value = "tasks", key= "#taskId")
    public TaskResponseDTO getTaskById(Long taskId){
        System.out.println("Fetching from DB...");
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return TaskMapper.toDTO(task);
    }
}