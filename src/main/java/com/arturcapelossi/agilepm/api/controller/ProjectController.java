package com.arturcapelossi.agilepm.api.controller;

import com.arturcapelossi.agilepm.api.dto.DtoMapper;
import com.arturcapelossi.agilepm.api.dto.request.AddMemberRequest;
import com.arturcapelossi.agilepm.api.dto.request.CreateProjectRequest;
import com.arturcapelossi.agilepm.api.dto.request.UpdateProjectRequest;
import com.arturcapelossi.agilepm.api.dto.response.ProjectResponse;
import com.arturcapelossi.agilepm.api.dto.response.UserResponse;
import com.arturcapelossi.agilepm.application.usecase.AddMemberToProjectUseCase;
import com.arturcapelossi.agilepm.application.usecase.CreateProjectUseCase;
import com.arturcapelossi.agilepm.application.usecase.DeleteProjectUseCase;
import com.arturcapelossi.agilepm.application.usecase.UpdateProjectUseCase;
import com.arturcapelossi.agilepm.domain.exception.ResourceNotFoundException;
import com.arturcapelossi.agilepm.domain.model.Project;
import com.arturcapelossi.agilepm.domain.model.User;
import com.arturcapelossi.agilepm.domain.repository.ProjectRepository;
import com.arturcapelossi.agilepm.domain.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Endpoints for managing projects")
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final DtoMapper dtoMapper;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UpdateProjectUseCase updateProjectUseCase;
    private final DeleteProjectUseCase deleteProjectUseCase;
    private final AddMemberToProjectUseCase addMemberToProjectUseCase;

    @PostMapping
    @Operation(summary = "Create a new project", description = "Creates a new project with the authenticated user as the owner.")
    public ResponseEntity<ProjectResponse> createProject(@RequestBody CreateProjectRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Project project = createProjectUseCase.execute(
                request.getName(),
                request.getDescription(),
                user.getId()
        );
        return new ResponseEntity<>(dtoMapper.toResponse(project), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieves all projects where the authenticated user is a member.")
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<ProjectResponse> projects = projectRepository.findAll().stream()
                .filter(project -> project.getMembers().stream()
                        .anyMatch(member -> member.getEmail().equals(email)))
                .map(dtoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieves a specific project by its ID if the user is a member.")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        boolean isMember = project.getMembers().stream()
                .anyMatch(member -> member.getEmail().equals(email));

        if (!isMember) {
            throw new AccessDeniedException("You are not a member of this project");
        }

        return ResponseEntity.ok(dtoMapper.toResponse(project));
    }

    @PostMapping("/{id}/members")
    @Operation(summary = "Add member to project", description = "Adds a user to the project by email.")
    public ResponseEntity<Void> addMember(@PathVariable UUID id, @RequestBody AddMemberRequest request) {
        addMemberToProjectUseCase.execute(id, request.getEmail());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/members")
    @Operation(summary = "Get project members", description = "Retrieves all members of a specific project.")
    public ResponseEntity<List<UserResponse>> getProjectMembers(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        boolean isMember = project.getMembers().stream()
                .anyMatch(member -> member.getEmail().equals(email));

        if (!isMember) {
            throw new AccessDeniedException("You are not a member of this project");
        }

        List<UserResponse> members = project.getMembers().stream()
                .map(dtoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(members);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project", description = "Updates an existing project.")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable UUID id,
            @RequestBody UpdateProjectRequest request) {
        Project project = updateProjectUseCase.execute(id, request);
        return ResponseEntity.ok(dtoMapper.toResponse(project));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project", description = "Deletes a project by its ID.")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        if (!project.getOwner().getEmail().equals(email)) {
            throw new AccessDeniedException("Only the project owner can delete the project");
        }

        deleteProjectUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
