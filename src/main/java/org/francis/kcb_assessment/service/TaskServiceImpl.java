package org.francis.kcb_assessment.service;

import lombok.RequiredArgsConstructor;
import org.francis.kcb_assessment.datatypes.TaskStatus;
import org.francis.kcb_assessment.dto.request.CreateTaskDTO;
import org.francis.kcb_assessment.dto.request.UpdateTaskDTO;
import org.francis.kcb_assessment.entity.Project;
import org.francis.kcb_assessment.entity.Task;
import org.francis.kcb_assessment.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;

    @Override
    public Task createTask(Long projectId, CreateTaskDTO createTaskDTO) {

        Project project = projectService.getProject(projectId);

        Task task = new Task();
        task.setProject(project);
        task.setTitle(createTaskDTO.getTitle());
        task.setDescription(createTaskDTO.getDescription());
        task.setDueDate(createTaskDTO.getDueDate());
        task.setStatus(TaskStatus.TO_DO);

        return taskRepository.save(task);
    }

    @Override
    public Page<Task> getTasks(Long projectId, TaskStatus taskStatus, LocalDate dueDate, Pageable pageable) {
        Project project = projectService.getProject(projectId);

        if (taskStatus != null && dueDate != null) {
            return taskRepository.findTaskByProjectAndStatusAndDueDate(project, taskStatus, dueDate, pageable);
        } else if (taskStatus != null) {
            return taskRepository.findTaskByProjectAndStatus(project, taskStatus, pageable);
        } else if (dueDate != null) {
            return taskRepository.findTaskByProjectAndDueDate(project, dueDate, pageable);
        } else {
            return taskRepository.findTaskByProject(project, pageable);
        }
    }

    @Override
    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    @Override
    public Task updateTask(Long taskId, UpdateTaskDTO updateTaskDTO) {

        Task task = getTask(taskId);

        task.setTitle(updateTaskDTO.getTitle());
        task.setDescription(updateTaskDTO.getDescription());
        task.setDueDate(updateTaskDTO.getDueDate());
        task.setStatus(updateTaskDTO.getStatus());

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = getTask(taskId);
        taskRepository.delete(task);
    }
}
