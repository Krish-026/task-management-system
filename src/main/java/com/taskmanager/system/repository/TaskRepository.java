package com.taskmanager.system.repository;

import com.taskmanager.system.entity.Task;
import com.taskmanager.system.entity.User;
import com.taskmanager.system.enums.TaskStatus;
import com.taskmanager.system.projection.TaskSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findByAssignedTo(User user);
    List<Task> findByCreatedBy(User user);
    Page<Task> findAll(Pageable pageable);
    List<Task> findByStatus(TaskStatus status);
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
    @Query(value = "SELECT t.id as id, t.title as title, t.status as status FROM Task t",
            countQuery = "SELECT count(t) FROM Task t")
    Page<TaskSummaryProjection> findAllProjectedBy(Pageable pageable);

    @Query("SELECT t FROM Task t JOIN FETCH t.createdBy JOIN FETCH t.assignedTo" )
    List<Task> findAllWithUsers();
}