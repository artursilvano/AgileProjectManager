package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Project;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.model.enums.StoryPriority;
import com.arturcapelossi.agilepm.domain.model.enums.StoryStatus;
import com.arturcapelossi.agilepm.domain.repository.ProjectRepository;
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
class CreateUserStoryUseCaseTest {

    @Mock
    private UserStoryRepository userStoryRepository;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private CreateUserStoryUseCase createUserStoryUseCase;

    private Project project;
    private UUID projectId;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
        project = new Project();
        project.setId(projectId);
    }

    @Test
    void execute_ShouldCreateUserStory_WhenProjectExists() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userStoryRepository.save(any(UserStory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserStory userStory = createUserStoryUseCase.execute(projectId, "Story Title", "Story Description", StoryPriority.HIGH);

        assertNotNull(userStory);
        assertEquals("Story Title", userStory.getTitle());
        assertEquals("Story Description", userStory.getDescription());
        assertEquals(StoryPriority.HIGH, userStory.getPriority());
        assertEquals(StoryStatus.BACKLOG, userStory.getStatus());
        assertEquals(project, userStory.getProject());
        verify(userStoryRepository).save(any(UserStory.class));
    }

    @Test
    void execute_ShouldThrowException_WhenProjectNotFound() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> createUserStoryUseCase.execute(projectId, "Story Title", "Story Description", StoryPriority.HIGH));
        verify(userStoryRepository, never()).save(any(UserStory.class));
    }
}

