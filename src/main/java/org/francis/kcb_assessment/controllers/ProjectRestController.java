package org.francis.kcb_assessment.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.francis.kcb_assessment.config.doc.SwaggerDocSamples;
import org.francis.kcb_assessment.datatypes.TaskStatus;
import org.francis.kcb_assessment.dto.request.CreateProjectDTO;
import org.francis.kcb_assessment.dto.request.CreateTaskDTO;
import org.francis.kcb_assessment.dto.request.UpdateTaskDTO;
import org.francis.kcb_assessment.dto.response.ProjectSummary;
import org.francis.kcb_assessment.entity.Project;
import org.francis.kcb_assessment.entity.Task;
import org.francis.kcb_assessment.service.ProjectService;
import org.francis.kcb_assessment.service.TaskService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("projects")
@RequiredArgsConstructor
public class ProjectRestController {

    private final ProjectService projectService;
    private final TaskService taskService;

    @PostMapping
    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(example = SwaggerDocSamples.CREATE_PROJECT_RESPONSE)))
    })
    public Project createProject(@RequestBody @Valid CreateProjectDTO createProjectDTO) {
        return projectService.create(createProjectDTO);
    }

    @GetMapping
    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(example = SwaggerDocSamples.GET_PROJECTS_RESPONSE)))
    })
    @PageableAsQueryParam
    public Page<Project> getProjects(@Parameter(hidden = true) Pageable pageable) {
        return projectService.getProjects(pageable);
    }

    @GetMapping(value = "/{projectId}")
    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(example = SwaggerDocSamples.GET_PROJECT_RESPONSE)))
    })
    public Project getProject(@PathVariable Long projectId) {
        return projectService.getProject(projectId);
    }


    @GetMapping(value = "/summary")
    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(example = SwaggerDocSamples.GET_PROJECT_SUMMARY_RESPONSE)))
    })
    @PageableAsQueryParam
    public Page<ProjectSummary> getProjectSummary(@Parameter(hidden = true) Pageable pageable) {
        return projectService.getProjectSummary(pageable);
    }

    @PostMapping(value = "/{projectId}/tasks")
    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(example = SwaggerDocSamples.CREATE_TASK_RESPONSE)))
    })
    public Task createTask(@RequestBody @Valid CreateTaskDTO createTaskDTO, @PathVariable Long projectId) {
        return taskService.createTask(projectId, createTaskDTO);
    }

    @GetMapping(value = "/{projectId}/tasks")
    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(example = SwaggerDocSamples.GET_TASKS_RESPONSE)))
    })
    @PageableAsQueryParam
    public Page<Task> getTasks(@PathVariable Long projectId,
                               @RequestParam(value = "status", required = false) TaskStatus taskStatus,
                               @RequestParam(value = "dueDate", required = false) LocalDate dueDate,
                               @Parameter(hidden = true) Pageable pageable) {
        return taskService.getTasks(projectId, taskStatus, dueDate, pageable);
    }

    @GetMapping(value = "/{projectId}/tasks/{taskId}")
    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(example = SwaggerDocSamples.GET_TASK_RESPONSE)))
    })
    public Task getTask(@PathVariable Long projectId, @PathVariable Long taskId) {
        return taskService.getTask(taskId);
    }

    @PutMapping(value = "/{projectId}/tasks/{taskId}")
    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(example = SwaggerDocSamples.UPDATE_TASK_RESPONSE)))
    })
    public Task updateTask(@RequestBody @Valid UpdateTaskDTO updateTaskDTO, @PathVariable Long projectId, @PathVariable Long taskId) {
        return taskService.updateTask(taskId, updateTaskDTO);
    }

    @DeleteMapping(value = "/{projectId}/tasks/{taskId}")
    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public void deleteTask(@PathVariable Long projectId, @PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }
}
