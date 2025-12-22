package com.arturcapelossi.agilepm.api.dto.request;

import com.arturcapelossi.agilepm.domain.model.enums.StoryPriority;
import com.arturcapelossi.agilepm.domain.model.enums.StoryStatus;
import lombok.Data;

@Data
public class UpdateUserStoryRequest {
    private String title;
    private String description;
    private StoryPriority priority;
    private StoryStatus status;
}
