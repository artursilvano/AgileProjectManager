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

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddMemberToProjectUseCaseTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AddMemberToProjectUseCase addMemberToProjectUseCase;

    private Project project;
    private User user;
    private UUID projectId;
    private String email;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
        email = "test@example.com";

        project = new Project();
        project.setId(projectId);
        project.setMembers(new HashSet<>());

        user = new User();
        user.setEmail(email);
    }

    @Test
    void execute_ShouldAddMemberToProject_WhenConditionsAreMet() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        addMemberToProjectUseCase.execute(projectId, email);

        assertTrue(project.getMembers().contains(user));
        verify(projectRepository).save(project);
    }

    @Test
    void execute_ShouldThrowException_WhenProjectNotFound() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addMemberToProjectUseCase.execute(projectId, email));
    }

    @Test
    void execute_ShouldThrowException_WhenUserNotFound() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addMemberToProjectUseCase.execute(projectId, email));
    }
}

