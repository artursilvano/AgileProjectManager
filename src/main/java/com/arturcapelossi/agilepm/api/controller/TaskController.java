package com.arturcapelossi.agilepm.api.controller;

import com.arturcapelossi.agilepm.api.dto.DtoMapper;
import com.arturcapelossi.agilepm.api.dto.request.CreateTaskRequest;
import com.arturcapelossi.agilepm.api.dto.response.TaskResponse;
import com.arturcapelossi.agilepm.application.usecase.AssignTaskUseCase;
import com.arturcapelossi.agilepm.application.usecase.CreateTaskUseCase;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Task;
import com.arturcapelossi.agilepm.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-stories/{userStoryId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final AssignTaskUseCase assignTaskUseCase;
    private final DtoMapper dtoMapper;
    private final TaskRepository taskRepository;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable UUID userStoryId,
            @RequestBody CreateTaskRequest request) {
        Task task = createTaskUseCase.execute(
                userStoryId,
                request.getTitle(),
                request.getDescription()
        );
        return new ResponseEntity<>(dtoMapper.toResponse(task), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(@PathVariable UUID userStoryId) {
        List<TaskResponse> tasks = taskRepository.findByUserStoryId(userStoryId).stream()
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        return ResponseEntity.ok(dtoMapper.toResponse(task));
    }

}
