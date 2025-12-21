package com.arturcapelossi.agilepm.domain.model;

import com.arturcapelossi.agilepm.domain.model.enums.StoryPriority;
import com.arturcapelossi.agilepm.domain.model.enums.StoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStory {
    private UUID id;
    private String title;
    private String description;
    private StoryPriority priority;
    private StoryStatus status;
    private Sprint sprint;
    private Project project;
    private List<Task> tasks = new ArrayList<>();
    private LocalDateTime createdAt;
}

