package com.arturcapelossi.agilepm.domain.model;

import com.arturcapelossi.agilepm.domain.model.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private User assignedTo;
    private UserStory userStory;
    private List<Comment> comments = new ArrayList<>();
    private LocalDateTime createdAt;
}

