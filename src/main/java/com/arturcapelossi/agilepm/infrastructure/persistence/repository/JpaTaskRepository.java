package com.arturcapelossi.agilepm.infrastructure.persistence.repository;

import com.arturcapelossi.agilepm.domain.model.Task;
import com.arturcapelossi.agilepm.domain.repository.TaskRepository;
import com.arturcapelossi.agilepm.infrastructure.persistence.mapper.CycleAvoidingMappingContext;
import com.arturcapelossi.agilepm.infrastructure.persistence.mapper.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaTaskRepository implements TaskRepository {

    private final SpringDataTaskRepository jpaRepo;
    private final TaskMapper mapper;
    private final CycleAvoidingMappingContext context;

    public JpaTaskRepository(SpringDataTaskRepository jpaRepo, TaskMapper mapper, CycleAvoidingMappingContext context) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
        this.context = context;
    }

    @Override
    public Task save(Task task) {
        var entity = mapper.toEntity(task, context);
        var savedEntity = jpaRepo.save(entity);
        return mapper.toModel(savedEntity, context);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return jpaRepo.findById(id).map(entity -> mapper.toModel(entity, context));
    }

    @Override
    public List<Task> findAll() {
        return jpaRepo.findAll().stream()
                .map(entity -> mapper.toModel(entity, context))
                .toList();
    }

    @Override
    public List<Task> findByUserStoryId(UUID userStoryId) {
        return mapper.toModel(jpaRepo.findAllByUserStory_Id(userStoryId), context);
    }

    @Override
    public void delete(UUID id) {
        jpaRepo.deleteById(id);
    }
}
