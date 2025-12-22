package com.arturcapelossi.agilepm.api.controller;

import com.arturcapelossi.agilepm.api.dto.DtoMapper;
import com.arturcapelossi.agilepm.api.dto.request.AssignTaskRequest;
import com.arturcapelossi.agilepm.api.dto.request.CreateTaskRequest;
import com.arturcapelossi.agilepm.api.dto.request.UpdateTaskRequest;
import com.arturcapelossi.agilepm.api.dto.response.TaskResponse;
import com.arturcapelossi.agilepm.application.usecase.*;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Task;
import com.arturcapelossi.agilepm.domain.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-stories/{userStoryId}/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Endpoints for managing tasks within a user story")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final AssignTaskUseCase assignTaskUseCase;
    private final DtoMapper dtoMapper;
    private final TaskRepository taskRepository;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final UnassignTaskUseCase unassignTaskUseCase;

    @PostMapping
    @Operation(summary = "Create a new task", description = "Creates a new task for a specific user story.")
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
    @Operation(summary = "Get all tasks", description = "Retrieves all tasks for a specific user story.")
    public ResponseEntity<List<TaskResponse>> getAllTasks(@PathVariable UUID userStoryId) {
        List<TaskResponse> tasks = taskRepository.findByUserStoryId(userStoryId).stream()
                .map(dtoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Get task by ID", description = "Retrieves a specific task by its ID.")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        return ResponseEntity.ok(dtoMapper.toResponse(task));
    }

    @PutMapping("/{taskId}")
    @Operation(summary = "Update task", description = "Updates an existing task.")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable UUID taskId,
            @RequestBody UpdateTaskRequest request) {
        Task task = updateTaskUseCase.execute(taskId, request);
        return ResponseEntity.ok(dtoMapper.toResponse(task));
    }

    @DeleteMapping("/{taskId}")
    @Operation(summary = "Delete task", description = "Deletes a task by its ID.")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID taskId) {
        deleteTaskUseCase.execute(taskId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/assign")
    @Operation(summary = "Assign task", description = "Assigns a task to a specific user.")
    public ResponseEntity<TaskResponse> assignTask(
            @PathVariable UUID taskId,
            @RequestBody AssignTaskRequest request) {

        Task task = assignTaskUseCase.execute(
                taskId,
                request.getUserId()
        );
        return new ResponseEntity<>(dtoMapper.toResponse(task), HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}/assign")
    @Operation(summary = "Unassign task", description = "Unassigns a task from any user.")
    public ResponseEntity<TaskResponse> unassignTask(@PathVariable UUID taskId) {
        Task task = unassignTaskUseCase.execute(taskId);
        return new ResponseEntity<>(dtoMapper.toResponse(task), HttpStatus.OK);
    }
}
