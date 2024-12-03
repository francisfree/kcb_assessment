package org.francis.kcb_assessment.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectSummary {
    private Long id;
    private String name;
    private String description;
    private List<TaskSummary> taskSummary;
}
