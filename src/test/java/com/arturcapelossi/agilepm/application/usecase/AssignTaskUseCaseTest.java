package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.BusinessRuleException;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Project;
import com.arturcapelossi.agilepm.domain.model.Task;
import com.arturcapelossi.agilepm.domain.model.User;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.repository.TaskRepository;
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
class AssignTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AssignTaskUseCase assignTaskUseCase;

    private Task task;
    private User user;
    private Project project;
    private UUID taskId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        userId = UUID.randomUUID();

        user = new User();
        user.setId(userId);

        project = new Project();
        project.setMembers(new HashSet<>());
        project.getMembers().add(user);

        UserStory userStory = new UserStory();
        userStory.setProject(project);

        task = new Task();
        task.setId(taskId);
        task.setUserStory(userStory);
    }

    @Test
    void execute_ShouldAssignTask_WhenUserIsMember() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task assignedTask = assignTaskUseCase.execute(taskId, userId);

        assertEquals(user, assignedTask.getAssignedTo());
        verify(taskRepository).save(task);
    }

    @Test
    void execute_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> assignTaskUseCase.execute(taskId, userId));
    }

    @Test
    void execute_ShouldThrowException_WhenUserNotFound() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> assignTaskUseCase.execute(taskId, userId));
    }

    @Test
    void execute_ShouldThrowException_WhenUserIsNotMember() {
        project.getMembers().clear();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(BusinessRuleException.class, () -> assignTaskUseCase.execute(taskId, userId));
    }
}

