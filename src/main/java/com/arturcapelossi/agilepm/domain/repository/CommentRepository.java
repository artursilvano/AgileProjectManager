package com.arturcapelossi.agilepm.domain.repository;

import com.arturcapelossi.agilepm.domain.model.Comment;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository {
    Comment save(Comment comment);
    Optional<Comment> findById(UUID id);
    void delete(UUID id);
}

