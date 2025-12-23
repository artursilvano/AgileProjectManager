package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Project;
import com.arturcapelossi.agilepm.domain.model.User;
import com.arturcapelossi.agilepm.domain.repository.ProjectRepository;
import com.arturcapelossi.agilepm.domain.repository.UserRepository;
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
class CreateProjectUseCaseTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateProjectUseCase createProjectUseCase;

    private User owner;
    private UUID ownerId;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        owner = new User();
        owner.setId(ownerId);
        owner.setName("Owner");
    }

    @Test
    void execute_ShouldCreateProject_WhenOwnerExists() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Project project = createProjectUseCase.execute("Project Name", "Description", ownerId);

        assertNotNull(project);
        assertEquals("Project Name", project.getName());
        assertEquals("Description", project.getDescription());
        assertEquals(owner, project.getOwner());
        assertTrue(project.getMembers().contains(owner));
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void execute_ShouldThrowException_WhenOwnerDoesNotExist() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> createProjectUseCase.execute("Project Name", "Description", ownerId));
        verify(projectRepository, never()).save(any(Project.class));
    }
}

