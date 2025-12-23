package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.BusinessRuleException;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Project;
import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.model.enums.StoryStatus;
import com.arturcapelossi.agilepm.domain.repository.SprintRepository;
import com.arturcapelossi.agilepm.domain.repository.UserStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddUserStoryToSprintUseCaseTest {

    @Mock
    private UserStoryRepository userStoryRepository;
    @Mock
    private SprintRepository sprintRepository;

    @InjectMocks
    private AddUserStoryToSprintUseCase addUserStoryToSprintUseCase;

    private UserStory userStory;
    private Sprint sprint;
    private Project project;
    private UUID userStoryId;
    private UUID sprintId;
    private UUID projectId;

    @BeforeEach
    void setUp() {
        userStoryId = UUID.randomUUID();
        sprintId = UUID.randomUUID();
        projectId = UUID.randomUUID();

        project = new Project();
        project.setId(projectId);

        userStory = new UserStory();
        userStory.setId(userStoryId);
        userStory.setProject(project);

        sprint = new Sprint();
        sprint.setId(sprintId);
        sprint.setProject(project);
    }

    @Test
    void execute_ShouldAddUserStoryToSprint_WhenConditionsAreMet() {
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.of(userStory));
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));
        when(userStoryRepository.save(any(UserStory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserStory updatedUserStory = addUserStoryToSprintUseCase.execute(userStoryId, sprintId);

        assertEquals(sprint, updatedUserStory.getSprint());
        assertEquals(StoryStatus.TODO, updatedUserStory.getStatus());
        verify(userStoryRepository).save(userStory);
    }

    @Test
    void execute_ShouldThrowException_WhenUserStoryNotFound() {
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addUserStoryToSprintUseCase.execute(userStoryId, sprintId));
    }

    @Test
    void execute_ShouldThrowException_WhenSprintNotFound() {
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.of(userStory));
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addUserStoryToSprintUseCase.execute(userStoryId, sprintId));
    }

    @Test
    void execute_ShouldThrowException_WhenProjectMismatch() {
        Project otherProject = new Project();
        otherProject.setId(UUID.randomUUID());
        sprint.setProject(otherProject);

        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.of(userStory));
        when(sprintRepository.findById(sprintId)).thenReturn(Optional.of(sprint));

        assertThrows(BusinessRuleException.class, () -> addUserStoryToSprintUseCase.execute(userStoryId, sprintId));
    }
}

