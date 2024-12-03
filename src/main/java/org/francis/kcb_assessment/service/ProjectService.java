package org.francis.kcb_assessment.service;

import org.francis.kcb_assessment.dto.request.CreateProjectDTO;
import org.francis.kcb_assessment.dto.response.ProjectSummary;
import org.francis.kcb_assessment.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    Project create(CreateProjectDTO createProjectDTO);

    Page<Project> getProjects(Pageable pageable);

    Project getProject(Long projectId);

    Page<ProjectSummary> getProjectSummary(Pageable pageable);
}
