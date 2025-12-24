package com.arturcapelossi.agilepm.domain.model;

import com.arturcapelossi.agilepm.domain.exception.BusinessRuleException;
import com.arturcapelossi.agilepm.domain.model.enums.TaskStatus;
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
public class Task {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private User assignedTo;
    private UserStory userStory;
    private List<Comment> comments = new ArrayList<>();
    private LocalDateTime createdAt;

    public static Task create(String title, String description, UserStory userStory) {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskStatus.TODO);
        task.setUserStory(userStory);
        task.setCreatedAt(LocalDateTime.now());
        return task;
    }

    public void assignTo(User user) {
        boolean isMember = this.userStory.getProject().getMembers().stream()
                .anyMatch(member -> member.getId().equals(user.getId()));

        if (!isMember) {
            throw new BusinessRuleException("User is not a member of the project.");
        }
        this.assignedTo = user;
    }

    public void unassign() {
        this.assignedTo = null;
    }

    public void update(String title, String description, TaskStatus status) {
        if (title != null) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (status != null) {
            if (this.userStory.getSprint() == null) {
                throw new BusinessRuleException("Cannot change status of a task if the user story is in backlog.");
            }
            this.status = status;
        }
    }
}
