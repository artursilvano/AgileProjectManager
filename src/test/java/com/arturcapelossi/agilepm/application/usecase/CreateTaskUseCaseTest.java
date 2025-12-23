package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Task;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.model.enums.TaskStatus;
import com.arturcapelossi.agilepm.domain.repository.TaskRepository;
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
class CreateTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserStoryRepository userStoryRepository;

    @InjectMocks
    private CreateTaskUseCase createTaskUseCase;

    private UserStory userStory;
    private UUID userStoryId;

    @BeforeEach
    void setUp() {
        userStoryId = UUID.randomUUID();
        userStory = new UserStory();
        userStory.setId(userStoryId);
    }

    @Test
    void execute_ShouldCreateTask_WhenUserStoryExists() {
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.of(userStory));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task task = createTaskUseCase.execute(userStoryId, "Task Title", "Task Description");

        assertNotNull(task);
        assertEquals("Task Title", task.getTitle());
        assertEquals("Task Description", task.getDescription());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertEquals(userStory, task.getUserStory());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void execute_ShouldThrowException_WhenUserStoryNotFound() {
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> createTaskUseCase.execute(userStoryId, "Task Title", "Task Description"));
        verify(taskRepository, never()).save(any(Task.class));
    }
}

