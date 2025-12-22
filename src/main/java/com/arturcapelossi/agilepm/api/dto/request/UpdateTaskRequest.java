package com.arturcapelossi.agilepm.api.dto.request;

import com.arturcapelossi.agilepm.domain.model.enums.TaskStatus;
import lombok.Data;

@Data
public class UpdateTaskRequest {
    private String title;
    private String description;
    private TaskStatus status;
}
