package com.arturcapelossi.agilepm.api.controller;

import com.arturcapelossi.agilepm.api.dto.DtoMapper;
import com.arturcapelossi.agilepm.api.dto.request.AddUserStoryToSprintRequest;
import com.arturcapelossi.agilepm.api.dto.request.CreateSprintRequest;
import com.arturcapelossi.agilepm.api.dto.request.UpdateSprintRequest;
import com.arturcapelossi.agilepm.api.dto.response.SprintResponse;
import com.arturcapelossi.agilepm.api.dto.response.UserStoryResponse;
import com.arturcapelossi.agilepm.application.usecase.*;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.repository.SprintRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects/{projectId}/sprints")
@RequiredArgsConstructor
@Tag(name = "Sprints", description = "Endpoints for managing sprints within a project")
public class SprintController {

    private final CreateSprintUseCase createSprintUseCase;
    private final StartSprintUseCase startSprintUseCase;
    private final CloseSprintUseCase closeSprintUseCase;
    private final AddUserStoryToSprintUseCase addUserStoryToSprintUseCase;
    private final RemoveUserStoryFromSprintUseCase removeUserStoryFromSprintUseCase;
    private final UpdateSprintUseCase updateSprintUseCase;
    private final DeleteSprintUseCase deleteSprintUseCase;
    private final DtoMapper dtoMapper;
    private final SprintRepository sprintRepository;

    @PostMapping
    @Operation(summary = "Create a new sprint", description = "Creates a new sprint for a specific project.")
    public ResponseEntity<SprintResponse> createSprint(
            @PathVariable UUID projectId,
            @RequestBody CreateSprintRequest request) {
        Sprint sprint = createSprintUseCase.execute(
                projectId,
                request.getName(),
                request.getStartDate(),
                request.getEndDate()
        );
        return new ResponseEntity<>(dtoMapper.toResponse(sprint), HttpStatus.CREATED);
    }

    @PostMapping("/{sprintId}/start")
    @Operation(summary = "Start a sprint", description = "Starts a planned sprint. Only one sprint can be active at a time.")
    public ResponseEntity<SprintResponse> startSprint(@PathVariable UUID sprintId) {
        Sprint sprint = startSprintUseCase.execute(sprintId);
        return new ResponseEntity<>(dtoMapper.toResponse(sprint), HttpStatus.OK);
    }

    @PostMapping("/{sprintId}/close")
    @Operation(summary = "Close a sprint", description = "Closes an active sprint.")
    public ResponseEntity<SprintResponse> closeSprint(@PathVariable UUID sprintId) {
        Sprint sprint = closeSprintUseCase.execute(sprintId);
        return new ResponseEntity<>(dtoMapper.toResponse(sprint), HttpStatus.OK);
    }

    @PutMapping("/{sprintId}")
    @Operation(summary = "Update sprint", description = "Updates an existing sprint.")
    public ResponseEntity<SprintResponse> updateSprint(
            @PathVariable UUID sprintId,
            @RequestBody UpdateSprintRequest request) {
        Sprint sprint = updateSprintUseCase.execute(sprintId, request);
        return ResponseEntity.ok(dtoMapper.toResponse(sprint));
    }

    @DeleteMapping("/{sprintId}")
    @Operation(summary = "Delete sprint", description = "Deletes a sprint by its ID.")
    public ResponseEntity<Void> deleteSprint(@PathVariable UUID sprintId) {
        deleteSprintUseCase.execute(sprintId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{sprintId}/user-stories")
    @Operation(summary = "Add user story to sprint", description = "Adds a user story to a specific sprint.")
    public ResponseEntity<UserStoryResponse> addUserStoryToSprint(
            @PathVariable UUID sprintId,
            @RequestBody AddUserStoryToSprintRequest request) {
        UserStory userStory = addUserStoryToSprintUseCase.execute(
                request.getUserStoryId(),
                sprintId
        );
        return new ResponseEntity<>(dtoMapper.toResponse(userStory), HttpStatus.OK);
    }

    @DeleteMapping("/{sprintId}/user-stories/{userStoryId}")
    @Operation(summary = "Remove user story from sprint", description = "Removes a user story from a specific sprint.")
    public ResponseEntity<UserStoryResponse> removeUserStoryFromSprint(
            @PathVariable UUID sprintId,
            @PathVariable UUID userStoryId) {
        UserStory userStory = removeUserStoryFromSprintUseCase.execute(sprintId, userStoryId);
        return new ResponseEntity<>(dtoMapper.toResponse(userStory), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all sprints", description = "Retrieves all sprints for a specific project.")
    public ResponseEntity<List<SprintResponse>> getAllSprints(@PathVariable UUID projectId) {
        List<SprintResponse> sprints = sprintRepository.findAll().stream()
                .filter(s -> s.getProject().getId().equals(projectId))
                .map(dtoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(sprints);
    }

    @GetMapping("/{sprintId}")
    @Operation(summary = "Get sprint by ID", description = "Retrieves a specific sprint by its ID.")
    public ResponseEntity<SprintResponse> getSprintById(@PathVariable UUID sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint not found with id: " + sprintId));
        return ResponseEntity.ok(dtoMapper.toResponse(sprint));
    }
}
