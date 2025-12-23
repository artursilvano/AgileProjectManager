package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Task;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private DeleteTaskUseCase deleteTaskUseCase;

    private Task task;
    private UUID taskId;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        task = new Task();
        task.setId(taskId);
    }

    @Test
    void execute_ShouldDeleteTask_WhenTaskExists() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        deleteTaskUseCase.execute(taskId);

        verify(taskRepository).delete(taskId);
    }

    @Test
    void execute_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> deleteTaskUseCase.execute(taskId));
        verify(taskRepository, never()).delete(any());
    }
}

