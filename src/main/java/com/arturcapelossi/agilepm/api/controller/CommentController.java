package com.arturcapelossi.agilepm.api.controller;

import com.arturcapelossi.agilepm.api.dto.DtoMapper;
import com.arturcapelossi.agilepm.api.dto.request.CreateCommentRequest;
import com.arturcapelossi.agilepm.api.dto.response.CommentResponse;
import com.arturcapelossi.agilepm.application.usecase.CreateCommentUseCase;
import com.arturcapelossi.agilepm.domain.model.Comment;
import com.arturcapelossi.agilepm.domain.repository.CommentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Endpoints for managing comments on tasks")
public class CommentController {

    private final CreateCommentUseCase createCommentUseCase;
    private final CommentRepository commentRepository;
    private final DtoMapper dtoMapper;

    @PostMapping
    @Operation(summary = "Create a comment", description = "Creates a new comment on a task.")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable UUID taskId,
            @RequestBody CreateCommentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Comment comment = createCommentUseCase.execute(taskId, email, request.getContent());
        return new ResponseEntity<>(dtoMapper.toResponse(comment), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all comments", description = "Retrieves all comments for a specific task.")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable UUID taskId) {
        List<CommentResponse> comments = commentRepository.findByTaskId(taskId).stream()
                .map(dtoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(comments);
    }
}

