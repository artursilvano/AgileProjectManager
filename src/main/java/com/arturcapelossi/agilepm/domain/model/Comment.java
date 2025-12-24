package com.arturcapelossi.agilepm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private UUID id;
    private String content;
    private User author;
    private Task task;
    private LocalDateTime createdAt;

    public static Comment create(String content, Task task, User author) {
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID());
        comment.setContent(content);
        comment.setTask(task);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());
        return comment;
    }
}
