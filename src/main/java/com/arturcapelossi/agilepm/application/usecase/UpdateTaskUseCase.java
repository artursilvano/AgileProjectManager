package com.arturcapelossi.agilepm.application.usecase;

import com.arturcapelossi.agilepm.api.dto.request.UpdateTaskRequest;
import com.arturcapelossi.agilepm.domain.exception.BusinessRuleException;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Task;
import com.arturcapelossi.agilepm.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateTaskUseCase {

    private final TaskRepository taskRepository;

    @Transactional
    public Task execute(UUID id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            if (task.getUserStory().getSprint() == null) {
                throw new BusinessRuleException("Cannot change status of a task if the user story is in backlog.");
            }
            task.setStatus(request.getStatus());
        }

        return taskRepository.save(task);
    }
}
