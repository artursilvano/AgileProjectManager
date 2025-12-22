package com.arturcapelossi.agilepm.api.controller;

import com.arturcapelossi.agilepm.api.dto.DtoMapper;
import com.arturcapelossi.agilepm.api.dto.request.CreateUserStoryRequest;
import com.arturcapelossi.agilepm.api.dto.request.UpdateUserStoryRequest;
import com.arturcapelossi.agilepm.api.dto.response.UserStoryResponse;
import com.arturcapelossi.agilepm.application.usecase.CreateUserStoryUseCase;
import com.arturcapelossi.agilepm.application.usecase.DeleteUserStoryUseCase;
import com.arturcapelossi.agilepm.application.usecase.UpdateUserStoryUseCase;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.repository.UserStoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects/{projectId}/user-stories")
@RequiredArgsConstructor
@Tag(name = "User Stories", description = "Endpoints for managing user stories within a project")
public class UserStoryController {

    private final CreateUserStoryUseCase createUserStoryUseCase;
    private final DtoMapper dtoMapper;
    private final UserStoryRepository userStoryRepository;
    private final UpdateUserStoryUseCase updateUserStoryUseCase;
    private final DeleteUserStoryUseCase deleteUserStoryUseCase;

    @PostMapping
    @Operation(summary = "Create a new user story", description = "Creates a new user story for a specific project.")
    public ResponseEntity<UserStoryResponse> createUserStory(
            @PathVariable UUID projectId,
            @RequestBody CreateUserStoryRequest request) {
        UserStory userStory = createUserStoryUseCase.execute(
                projectId,
                request.getTitle(),
                request.getDescription(),
                request.getPriority()
        );
        return new ResponseEntity<>(dtoMapper.toResponse(userStory), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all user stories", description = "Retrieves all user stories for a specific project.")
    public ResponseEntity<List<UserStoryResponse>> getAllUserStories(@PathVariable UUID projectId) {
        List<UserStoryResponse> userStories = userStoryRepository.findAll().stream()
                .filter(us -> us.getProject().getId().equals(projectId))
                .map(dtoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(userStories);
    }

    @GetMapping("/{userStoryId}")
    @Operation(summary = "Get user story by ID", description = "Retrieves a specific user story by its ID.")
    public ResponseEntity<UserStoryResponse> getUserStoryById(@PathVariable UUID userStoryId) {
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new ResourceNotFoundException("User Story not found with id: " + userStoryId));
        return ResponseEntity.ok(dtoMapper.toResponse(userStory));
    }

    @PutMapping("/{userStoryId}")
    @Operation(summary = "Update user story", description = "Updates an existing user story.")
    public ResponseEntity<UserStoryResponse> updateUserStory(
            @PathVariable UUID userStoryId,
            @RequestBody UpdateUserStoryRequest request) {
        UserStory userStory = updateUserStoryUseCase.execute(userStoryId, request);
        return ResponseEntity.ok(dtoMapper.toResponse(userStory));
    }

    @DeleteMapping("/{userStoryId}")
    @Operation(summary = "Delete user story", description = "Deletes a user story by its ID.")
    public ResponseEntity<Void> deleteUserStory(@PathVariable UUID userStoryId) {
        deleteUserStoryUseCase.execute(userStoryId);
        return ResponseEntity.noContent().build();
    }
}
