package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Task;
import com.arturcapelossi.agilepm.domain.model.User;
import com.arturcapelossi.agilepm.domain.repository.TaskRepository;
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
class UnassignTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private UnassignTaskUseCase unassignTaskUseCase;

    private Task task;
    private UUID taskId;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        task = new Task();
        task.setId(taskId);
        task.setAssignedTo(new User());
    }

    @Test
    void execute_ShouldUnassignTask_WhenTaskExists() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task unassignedTask = unassignTaskUseCase.execute(taskId);

        assertNull(unassignedTask.getAssignedTo());
        verify(taskRepository).save(task);
    }

    @Test
    void execute_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> unassignTaskUseCase.execute(taskId));
    }
}

