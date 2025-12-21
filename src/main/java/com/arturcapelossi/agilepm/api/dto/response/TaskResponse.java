package com.arturcapelossi.agilepm.api.dto.response;

import com.arturcapelossi.agilepm.domain.model.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private UserResponse assignedTo;
    private UUID userStoryId;
    private LocalDateTime createdAt;
}

