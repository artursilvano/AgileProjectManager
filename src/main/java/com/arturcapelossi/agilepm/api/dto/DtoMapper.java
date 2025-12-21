package com.arturcapelossi.agilepm.api.dto;

import com.arturcapelossi.agilepm.api.dto.response.*;
import com.arturcapelossi.agilepm.domain.model.*;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public UserResponse toResponse(User user) {
        if (user == null) return null;
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        return response;
    }

    public ProjectResponse toResponse(Project project) {
        if (project == null) return null;
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setOwner(toResponse(project.getOwner()));
        response.setCreatedAt(project.getCreatedAt());
        return response;
    }

    public SprintResponse toResponse(Sprint sprint) {
        if (sprint == null) return null;
        SprintResponse response = new SprintResponse();
        response.setId(sprint.getId());
        response.setName(sprint.getName());
        response.setStartDate(sprint.getStartDate());
        response.setEndDate(sprint.getEndDate());
        response.setStatus(sprint.getStatus());
        if (sprint.getProject() != null) {
            response.setProjectId(sprint.getProject().getId());
        }
        response.setCreatedAt(sprint.getCreatedAt());
        return response;
    }

    public UserStoryResponse toResponse(UserStory userStory) {
        if (userStory == null) return null;
        UserStoryResponse response = new UserStoryResponse();
        response.setId(userStory.getId());
        response.setTitle(userStory.getTitle());
        response.setDescription(userStory.getDescription());
        response.setPriority(userStory.getPriority());
        response.setStatus(userStory.getStatus());
        if (userStory.getSprint() != null) {
            response.setSprintId(userStory.getSprint().getId());
        }
        if (userStory.getProject() != null) {
            response.setProjectId(userStory.getProject().getId());
        }
        response.setCreatedAt(userStory.getCreatedAt());
        return response;
    }

    public TaskResponse toResponse(Task task) {
        if (task == null) return null;
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setAssignedTo(toResponse(task.getAssignedTo()));
        if (task.getUserStory() != null) {
            response.setUserStoryId(task.getUserStory().getId());
        }
        response.setCreatedAt(task.getCreatedAt());
        return response;
    }
}

