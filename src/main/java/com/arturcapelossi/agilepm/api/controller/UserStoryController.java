package com.arturcapelossi.agilepm.api.controller;

import com.arturcapelossi.agilepm.api.dto.DtoMapper;
import com.arturcapelossi.agilepm.api.dto.request.CreateUserStoryRequest;
import com.arturcapelossi.agilepm.api.dto.response.UserStoryResponse;
import com.arturcapelossi.agilepm.application.usecase.CreateUserStoryUseCase;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.repository.UserStoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects/{projectId}/user-stories")
@RequiredArgsConstructor
public class UserStoryController {

    private final CreateUserStoryUseCase createUserStoryUseCase;
    private final DtoMapper dtoMapper;
    private final UserStoryRepository userStoryRepository;

    @PostMapping
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
    public ResponseEntity<List<UserStoryResponse>> getAllUserStories(@PathVariable UUID projectId) {
        List<UserStoryResponse> userStories = userStoryRepository.findAll().stream()
                .filter(us -> us.getProject().getId().equals(projectId))
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userStories);
    }

    @GetMapping("/{userStoryId}")
    public ResponseEntity<UserStoryResponse> getUserStoryById(@PathVariable UUID userStoryId) {
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new ResourceNotFoundException("UserStory not found with id: " + userStoryId));
        return ResponseEntity.ok(dtoMapper.toResponse(userStory));
    }
}
