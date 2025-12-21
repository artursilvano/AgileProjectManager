package com.arturcapelossi.agilepm.api.dto.request;

import com.arturcapelossi.agilepm.domain.model.enums.StoryPriority;
import lombok.Data;

@Data
public class CreateUserStoryRequest {
    private String title;
    private String description;
    private StoryPriority priority;
}

