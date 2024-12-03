package org.francis.kcb_assessment.service;

import lombok.RequiredArgsConstructor;
import org.francis.kcb_assessment.dto.request.CreateProjectDTO;
import org.francis.kcb_assessment.dto.response.ProjectSummary;
import org.francis.kcb_assessment.dto.response.TaskSummary;
import org.francis.kcb_assessment.entity.Project;
import org.francis.kcb_assessment.repository.ProjectRepository;
import org.francis.kcb_assessment.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Override
    public Project create(CreateProjectDTO createProjectDTO) {

        boolean nameExist = projectRepository.findProjectByNameIgnoreCase(createProjectDTO.getName()).isPresent();

        if (nameExist) {
            throw new IllegalArgumentException("project name exist");
        }

        Project project = new Project();
        project.setName(createProjectDTO.getName());
        project.setDescription(createProjectDTO.getDescription());

        return projectRepository.save(project);
    }

    @Override
    public Page<Project> getProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Override
    public Project getProject(Long projectId) {
        return projectRepository.findProjectById(projectId).orElseThrow(() -> new IllegalArgumentException("project not found"));
    }

    @Override
    public Page<ProjectSummary> getProjectSummary(Pageable pageable) {
        List<Project> projects = getProjects(pageable).getContent();

        List<ProjectSummary> projectSummaryList = projects.stream().map(project -> {
            List<TaskSummary> taskSummaryList = taskRepository.getTaskSummaryByProject(project);
            ProjectSummary projectSummary = new ProjectSummary();
            projectSummary.setId(project.getId());
            projectSummary.setName(projectSummary.getName());
            projectSummary.setDescription(project.getDescription());
            projectSummary.setTaskSummary(taskSummaryList);
            return projectSummary;
        }).collect(Collectors.toList());

        return new PageImpl<>(projectSummaryList, pageable, projectSummaryList.size());
    }
}
