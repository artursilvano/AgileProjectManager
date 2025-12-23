package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Project;
import com.arturcapelossi.agilepm.domain.repository.ProjectRepository;
import com.arturcapelossi.agilepm.domain.repository.SprintRepository;
import com.arturcapelossi.agilepm.domain.repository.UserStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteProjectUseCaseTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private SprintRepository sprintRepository;
    @Mock
    private UserStoryRepository userStoryRepository;

    @InjectMocks
    private DeleteProjectUseCase deleteProjectUseCase;

    private Project project;
    private UUID projectId;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
        project = new Project();
        project.setId(projectId);
    }

    @Test
    void execute_ShouldDeleteProject_WhenProjectExists() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(sprintRepository.findAll()).thenReturn(Collections.emptyList());
        when(userStoryRepository.findAll()).thenReturn(Collections.emptyList());

        deleteProjectUseCase.execute(projectId);

        verify(projectRepository).delete(projectId);
    }

    @Test
    void execute_ShouldThrowException_WhenProjectNotFound() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> deleteProjectUseCase.execute(projectId));
        verify(projectRepository, never()).delete(any());
    }
}

