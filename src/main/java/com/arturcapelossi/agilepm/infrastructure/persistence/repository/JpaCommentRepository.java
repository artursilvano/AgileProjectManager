package com.arturcapelossi.agilepm.infrastructure.persistence.repository;

import com.arturcapelossi.agilepm.domain.model.Comment;
import com.arturcapelossi.agilepm.domain.repository.CommentRepository;
import com.arturcapelossi.agilepm.infrastructure.persistence.mapper.CommentMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class JpaCommentRepository implements CommentRepository {

    private final SpringDataCommentRepository jpaRepo;
    private final CommentMapper mapper;

    public JpaCommentRepository(SpringDataCommentRepository jpaRepo, CommentMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public Comment save(Comment comment) {
        var entity = mapper.toEntity(comment);
        var savedEntity = jpaRepo.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public Optional<Comment> findById(UUID id) {
        return jpaRepo.findById(id).map(mapper::toModel);
    }

    @Override
    public void delete(UUID id) {
        jpaRepo.deleteById(id);
    }
}
