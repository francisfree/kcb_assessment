package org.francis.kcb_assessment.repository;

import org.francis.kcb_assessment.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findProjectById(Long id);

    Optional<Project> findProjectByNameIgnoreCase(String name);
}
