package com.arturcapelossi.agilepm.infrastructure.persistence.repository;

import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.repository.UserStoryRepository;
import com.arturcapelossi.agilepm.infrastructure.persistence.mapper.CycleAvoidingMappingContext;
import com.arturcapelossi.agilepm.infrastructure.persistence.mapper.UserStoryMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaUserStoryRepository implements UserStoryRepository {

    private final SpringDataUserStoryRepository jpaRepo;
    private final UserStoryMapper mapper;
    private final CycleAvoidingMappingContext context;

    public JpaUserStoryRepository(SpringDataUserStoryRepository jpaRepo, UserStoryMapper mapper, CycleAvoidingMappingContext context) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
        this.context = context;
    }

    @Override
    public UserStory save(UserStory userStory) {
        var entity = mapper.toEntity(userStory, context);
        var savedEntity = jpaRepo.save(entity);
        return mapper.toModel(savedEntity, context);
    }

    @Override
    public Optional<UserStory> findById(UUID id) {
        return jpaRepo.findById(id).map(entity -> mapper.toModel(entity, context));
    }

    @Override
    public List<UserStory> findAll() {
        return jpaRepo.findAll().stream()
                .map(entity -> mapper.toModel(entity, context))
                .toList();
    }

    @Override
    public void delete(UUID id) {
        jpaRepo.deleteById(id);
    }
}
