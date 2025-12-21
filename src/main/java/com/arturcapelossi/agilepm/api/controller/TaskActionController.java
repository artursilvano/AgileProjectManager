package com.arturcapelossi.agilepm.api.controller;

import com.arturcapelossi.agilepm.api.dto.DtoMapper;
import com.arturcapelossi.agilepm.api.dto.request.AssignTaskRequest;
import com.arturcapelossi.agilepm.api.dto.response.TaskResponse;
import com.arturcapelossi.agilepm.application.usecase.AssignTaskUseCase;
import com.arturcapelossi.agilepm.domain.model.Task;
import com.arturcapelossi.agilepm.domain.model.User;
import com.arturcapelossi.agilepm.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskActionController {

    private final AssignTaskUseCase assignTaskUseCase;
    private final DtoMapper dtoMapper;
    private final UserRepository userRepository;

    @PostMapping("/{taskId}/assign")
    public ResponseEntity<TaskResponse> assignTask(
            @PathVariable UUID taskId,
            @RequestBody AssignTaskRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Task task = assignTaskUseCase.execute(
                taskId,
                user.getId()
        );
        return new ResponseEntity<>(dtoMapper.toResponse(task), HttpStatus.OK);
    }
}
