package org.francis.kcb_assessment.service;

import org.francis.kcb_assessment.datatypes.TaskStatus;
import org.francis.kcb_assessment.dto.request.CreateTaskDTO;
import org.francis.kcb_assessment.dto.request.UpdateTaskDTO;
import org.francis.kcb_assessment.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TaskService {
    Task createTask(Long projectId, CreateTaskDTO createTaskDTO);

    Page<Task> getTasks(Long projectId, TaskStatus taskStatus, LocalDate dueDate, Pageable pageable);

    Task getTask(Long taskId);

    Task updateTask(Long taskId, UpdateTaskDTO updateTaskDTO);

    void deleteTask(Long taskId);
}
