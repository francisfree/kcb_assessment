package org.francis.kcb_assessment.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.francis.kcb_assessment.datatypes.TaskStatus;

@Getter
@Setter
public class TaskSummary {
    private TaskStatus taskStatus;
    private Long count;

    public TaskSummary(TaskStatus taskStatus, Long count) {
        this.taskStatus = taskStatus;
        this.count = count;
    }
}
