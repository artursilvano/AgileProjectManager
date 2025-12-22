package com.arturcapelossi.agilepm.infrastructure.persistence.repository;

import com.arturcapelossi.agilepm.domain.model.Comment;
import com.arturcapelossi.agilepm.domain.repository.CommentRepository;
import com.arturcapelossi.agilepm.infrastructure.persistence.mapper.CommentMapper;
import com.arturcapelossi.agilepm.infrastructure.persistence.mapper.CycleAvoidingMappingContext;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class JpaCommentRepository implements CommentRepository {

    private final SpringDataCommentRepository jpaRepo;
    private final CommentMapper mapper;
    private final CycleAvoidingMappingContext context;

    public JpaCommentRepository(SpringDataCommentRepository jpaRepo, CommentMapper mapper, CycleAvoidingMappingContext context) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
        this.context = context;
    }

    @Override
    public Comment save(Comment comment) {
        var entity = mapper.toEntity(comment, context);
        var savedEntity = jpaRepo.save(entity);
        return mapper.toModel(savedEntity, context);
    }

    @Override
    public Optional<Comment> findById(UUID id) {
        return jpaRepo.findById(id).map(entity -> mapper.toModel(entity, context));
    }

    @Override
    public void delete(UUID id) {
        jpaRepo.deleteById(id);
    }

    @Override
    public java.util.List<Comment> findByTaskId(UUID taskId) {
        return jpaRepo.findByTask_Id(taskId).stream()
                .map(entity -> mapper.toModel(entity, context))
                .toList();
    }
}
