package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Task;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.model.enums.TaskStatus;
import com.arturcapelossi.agilepm.domain.repository.TaskRepository;
import com.arturcapelossi.agilepm.domain.repository.UserStoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateTaskUseCase {

    private final TaskRepository taskRepository;
    private final UserStoryRepository userStoryRepository;

    public Task execute(UUID userStoryId, String title, String description) {
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new ResourceNotFoundException("UserStory not found with id: " + userStoryId));

        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskStatus.TODO);
        task.setUserStory(userStory);
        task.setCreatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }
}

