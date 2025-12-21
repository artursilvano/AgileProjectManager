package com.arturcapelossi.agilepm.infrastructure.persistence.repository;

import com.arturcapelossi.agilepm.infrastructure.persistence.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataCommentRepository extends JpaRepository<CommentEntity, UUID> {
}

