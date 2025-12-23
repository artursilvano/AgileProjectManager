package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.api.dto.request.UpdateTaskRequest;
import com.arturcapelossi.agilepm.domain.exception.BusinessRuleException;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.domain.model.Task;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.model.enums.TaskStatus;
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
class UpdateTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private UpdateTaskUseCase updateTaskUseCase;

    private Task task;
    private UserStory userStory;
    private UUID taskId;
    private UpdateTaskRequest request;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        userStory = new UserStory();
        userStory.setSprint(new Sprint());

        task = new Task();
        task.setId(taskId);
        task.setUserStory(userStory);
        task.setTitle("Old Title");
        task.setStatus(TaskStatus.TODO);

        request = new UpdateTaskRequest();
        request.setTitle("New Title");
        request.setStatus(TaskStatus.DOING);
    }

    @Test
    void execute_ShouldUpdateTask_WhenConditionsAreMet() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task updatedTask = updateTaskUseCase.execute(taskId, request);

        assertEquals("New Title", updatedTask.getTitle());
        assertEquals(TaskStatus.DOING, updatedTask.getStatus());
        verify(taskRepository).save(task);
    }

    @Test
    void execute_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> updateTaskUseCase.execute(taskId, request));
    }

    @Test
    void execute_ShouldThrowException_WhenUpdatingStatusAndUserStoryInBacklog() {
        userStory.setSprint(null);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        assertThrows(BusinessRuleException.class, () -> updateTaskUseCase.execute(taskId, request));
    }
}

