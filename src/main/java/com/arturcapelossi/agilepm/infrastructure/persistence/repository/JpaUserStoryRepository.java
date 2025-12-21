package com.arturcapelossi.agilepm.infrastructure.persistence.repository;

import com.arturcapelossi.agilepm.domain.model.UserStory;
import com.arturcapelossi.agilepm.domain.repository.UserStoryRepository;
import com.arturcapelossi.agilepm.infrastructure.persistence.mapper.UserStoryMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaUserStoryRepository implements UserStoryRepository {

    private final SpringDataUserStoryRepository jpaRepo;
    private final UserStoryMapper mapper;

    public JpaUserStoryRepository(SpringDataUserStoryRepository jpaRepo, UserStoryMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public UserStory save(UserStory userStory) {
        var entity = mapper.toEntity(userStory);
        var savedEntity = jpaRepo.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public Optional<UserStory> findById(UUID id) {
        return jpaRepo.findById(id).map(mapper::toModel);
    }

    @Override
    public List<UserStory> findAll() {
        return jpaRepo.findAll().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        jpaRepo.deleteById(id);
    }
}
