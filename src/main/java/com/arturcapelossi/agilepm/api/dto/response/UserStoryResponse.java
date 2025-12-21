package com.arturcapelossi.agilepm.api.dto.response;

import com.arturcapelossi.agilepm.domain.model.enums.StoryPriority;
import com.arturcapelossi.agilepm.domain.model.enums.StoryStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserStoryResponse {
    private UUID id;
    private String title;
    private String description;
    private StoryPriority priority;
    private StoryStatus status;
    private UUID sprintId;
    private UUID projectId;
    private LocalDateTime createdAt;
}

