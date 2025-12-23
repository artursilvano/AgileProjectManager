package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Project;
import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.domain.model.enums.SprintStatus;
import com.arturcapelossi.agilepm.domain.repository.ProjectRepository;
import com.arturcapelossi.agilepm.domain.repository.SprintRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateSprintUseCaseTest {

    @Mock
    private SprintRepository sprintRepository;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private CreateSprintUseCase createSprintUseCase;

    private Project project;
    private UUID projectId;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
        project = new Project();
        project.setId(projectId);
    }

    @Test
    void execute_ShouldCreateSprint_WhenProjectExists() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(sprintRepository.save(any(Sprint.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Sprint sprint = createSprintUseCase.execute(projectId, "Sprint 1", LocalDate.now(), LocalDate.now().plusDays(14));

        assertNotNull(sprint);
        assertEquals("Sprint 1", sprint.getName());
        assertEquals(SprintStatus.PLANNED, sprint.getStatus());
        assertEquals(project, sprint.getProject());
        verify(sprintRepository).save(any(Sprint.class));
    }

    @Test
    void execute_ShouldThrowException_WhenProjectDoesNotExist() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> createSprintUseCase.execute(projectId, "Sprint 1", LocalDate.now(), LocalDate.now().plusDays(14)));
        verify(sprintRepository, never()).save(any(Sprint.class));
    }
}

