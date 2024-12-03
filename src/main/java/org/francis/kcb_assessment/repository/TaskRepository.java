package org.francis.kcb_assessment.repository;

import org.francis.kcb_assessment.datatypes.TaskStatus;
import org.francis.kcb_assessment.dto.response.TaskSummary;
import org.francis.kcb_assessment.entity.Project;
import org.francis.kcb_assessment.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findTaskByProject(Project project, Pageable pageable);

    Page<Task> findTaskByProjectAndStatus(Project project, TaskStatus taskStatus, Pageable pageable);

    Page<Task> findTaskByProjectAndDueDate(Project project, LocalDate date, Pageable pageable);

    Page<Task> findTaskByProjectAndStatusAndDueDate(Project project, TaskStatus taskStatus, LocalDate date, Pageable pageable);

    @Query("select NEW org.francis.kcb_assessment.dto.response.TaskSummary(t.status, count(t)) from Task t where t.project = ?1 group by t.status")
    List<TaskSummary> getTaskSummaryByProject(Project project);
}
