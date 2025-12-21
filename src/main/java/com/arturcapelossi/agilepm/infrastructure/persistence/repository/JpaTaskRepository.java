package com.arturcapelossi.agilepm.infrastructure.persistence.repository;

import com.arturcapelossi.agilepm.domain.model.Task;
import com.arturcapelossi.agilepm.domain.repository.TaskRepository;
import com.arturcapelossi.agilepm.infrastructure.persistence.mapper.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaTaskRepository implements TaskRepository {

    private final SpringDataTaskRepository jpaRepo;
    private final TaskMapper mapper;

    public JpaTaskRepository(SpringDataTaskRepository jpaRepo, TaskMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public Task save(Task task) {
        var entity = mapper.toEntity(task);
        var savedEntity = jpaRepo.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return jpaRepo.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Task> findAll() {
        return jpaRepo.findAll().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByUserStoryId(UUID userStoryId) {
        return mapper.toModel(jpaRepo.findAllByUserStory_Id(userStoryId));
    }

    @Override
    public void delete(UUID id) {
        jpaRepo.deleteById(id);
    }
}
