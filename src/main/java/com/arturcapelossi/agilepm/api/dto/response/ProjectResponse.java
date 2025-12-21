package com.arturcapelossi.agilepm.api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProjectResponse {
    private UUID id;
    private String name;
    private String description;
    private UserResponse owner;
    private LocalDateTime createdAt;
}

