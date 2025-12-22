package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Project;
import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.repository.ProjectRepository;
import com.arturcapelossi.agilepm.domain.repository.SprintRepository;
import com.arturcapelossi.agilepm.domain.repository.UserStoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeleteProjectUseCase {

    private final ProjectRepository projectRepository;
    private final SprintRepository sprintRepository;
    private final UserStoryRepository userStoryRepository;

    @Transactional
    public void execute(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        // Delete all sprints associated with the project
        List<Sprint> sprints = sprintRepository.findAll().stream()
                .filter(s -> s.getProject().getId().equals(id))
                .toList();
        sprints.forEach(s -> sprintRepository.delete(s.getId()));

        // Delete all user stories associated with the project
        List<UserStory> userStories = userStoryRepository.findAll().stream()
                .filter(us -> us.getProject().getId().equals(id))
                .toList();
        userStories.forEach(us -> userStoryRepository.delete(us.getId()));

        projectRepository.delete(project.getId());
    }
}
