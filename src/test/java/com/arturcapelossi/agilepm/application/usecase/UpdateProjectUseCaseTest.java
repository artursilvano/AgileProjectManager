package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.api.dto.request.UpdateProjectRequest;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Project;
import com.arturcapelossi.agilepm.domain.repository.ProjectRepository;
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
class UpdateProjectUseCaseTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private UpdateProjectUseCase updateProjectUseCase;

    private Project project;
    private UUID projectId;
    private UpdateProjectRequest request;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
        project = new Project();
        project.setId(projectId);
        project.setName("Old Name");
        project.setDescription("Old Description");

        request = new UpdateProjectRequest();
        request.setName("New Name");
        request.setDescription("New Description");
    }

    @Test
    void execute_ShouldUpdateProject_WhenProjectExists() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Project updatedProject = updateProjectUseCase.execute(projectId, request);

        assertEquals("New Name", updatedProject.getName());
        assertEquals("New Description", updatedProject.getDescription());
        verify(projectRepository).save(project);
    }

    @Test
    void execute_ShouldThrowException_WhenProjectNotFound() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> updateProjectUseCase.execute(projectId, request));
    }
}

