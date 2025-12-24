package com.arturcapelossi.agilepm.domain.model;

import com.arturcapelossi.agilepm.domain.exception.BusinessRuleException;
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

    public static UserStory create(String title, String description, StoryPriority priority, Project project) {
        UserStory userStory = new UserStory();
        userStory.setId(UUID.randomUUID());
        userStory.setTitle(title);
        userStory.setDescription(description);
        userStory.setPriority(priority);
        userStory.setStatus(StoryStatus.BACKLOG); // Default status
        userStory.setProject(project);
        userStory.setCreatedAt(LocalDateTime.now());
        return userStory;
    }

    public void addToSprint(Sprint sprint) {
        if (!this.project.getId().equals(sprint.getProject().getId())) {
            throw new BusinessRuleException("UserStory and Sprint must belong to the same project.");
        }
        this.sprint = sprint;
        this.status = StoryStatus.TODO;
    }

    public void removeFromSprint(UUID sprintId) {
        if (this.sprint == null || !this.sprint.getId().equals(sprintId)) {
            throw new BusinessRuleException("User Story is not assigned to this sprint.");
        }
        this.sprint = null;
        this.status = StoryStatus.BACKLOG;
    }

    public void update(String title, String description, StoryPriority priority, StoryStatus status) {
        if (title != null) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (priority != null) {
            this.priority = priority;
        }
        if (status != null) {
            if (this.sprint == null && status != StoryStatus.BACKLOG) {
                throw new BusinessRuleException("Cannot change status of a user story in backlog unless it is assigned to a sprint.");
            }
            if (status == StoryStatus.BACKLOG) {
                this.sprint = null;
            }
            this.status = status;
        }
    }
}
