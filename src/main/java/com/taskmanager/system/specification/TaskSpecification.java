package com.taskmanager.system.specification;

import com.taskmanager.system.entity.Task;
import com.taskmanager.system.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {
    public static Specification<Task> hasStatus(TaskStatus status){
        return ((root, query, criteriaBuilder) -> {
            if(status == null){
                return null;
            }
            return criteriaBuilder.equal(
                    root.get("status"),
                    status
            );
        });
    }

    public static Specification<Task> titleContains(String title){
        return ((root, query, criteriaBuilder) -> {
            if (title == null || title.isBlank()){
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + title.toLowerCase() + "%"
            );
        });
    }
}
