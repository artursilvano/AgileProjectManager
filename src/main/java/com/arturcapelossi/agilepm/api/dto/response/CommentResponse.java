package com.arturcapelossi.agilepm.api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommentResponse {
    private UUID id;
    private String content;
    private UserResponse author;
    private LocalDateTime createdAt;
}

