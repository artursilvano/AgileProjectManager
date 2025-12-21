package com.arturcapelossi.agilepm.api.controller;

import com.arturcapelossi.agilepm.api.dto.DtoMapper;
import com.arturcapelossi.agilepm.api.dto.request.AddUserStoryToSprintRequest;
import com.arturcapelossi.agilepm.api.dto.request.CreateSprintRequest;
import com.arturcapelossi.agilepm.api.dto.response.SprintResponse;
import com.arturcapelossi.agilepm.api.dto.response.UserStoryResponse;
import com.arturcapelossi.agilepm.application.usecase.AddUserStoryToSprintUseCase;
import com.arturcapelossi.agilepm.application.usecase.CreateSprintUseCase;
import com.arturcapelossi.agilepm.application.usecase.StartSprintUseCase;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects/{projectId}/sprints")
@RequiredArgsConstructor
public class SprintController {

    private final CreateSprintUseCase createSprintUseCase;
    private final StartSprintUseCase startSprintUseCase;
    private final AddUserStoryToSprintUseCase addUserStoryToSprintUseCase;
    private final DtoMapper dtoMapper;
    private final SprintRepository sprintRepository;

    @PostMapping
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
    public ResponseEntity<SprintResponse> startSprint(@PathVariable UUID sprintId) {
        Sprint sprint = startSprintUseCase.execute(sprintId);
        return new ResponseEntity<>(dtoMapper.toResponse(sprint), HttpStatus.OK);
    }

    @PostMapping("/{sprintId}/user-stories")
    public ResponseEntity<UserStoryResponse> addUserStoryToSprint(
            @PathVariable UUID sprintId,
            @RequestBody AddUserStoryToSprintRequest request) {
        UserStory userStory = addUserStoryToSprintUseCase.execute(
                request.getUserStoryId(),
                sprintId
        );
        return new ResponseEntity<>(dtoMapper.toResponse(userStory), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SprintResponse>> getAllSprints(@PathVariable UUID projectId) {
        List<SprintResponse> sprints = sprintRepository.findAll().stream()
                .filter(s -> s.getProject().getId().equals(projectId))
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sprints);
    }

    @GetMapping("/{sprintId}")
    public ResponseEntity<SprintResponse> getSprintById(@PathVariable UUID sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint not found with id: " + sprintId));
        return ResponseEntity.ok(dtoMapper.toResponse(sprint));
    }
}
