package com.arturcapelossi.agilepm.infrastructure.persistence.repository;

import com.arturcapelossi.agilepm.domain.model.Sprint;
import com.arturcapelossi.agilepm.domain.repository.SprintRepository;
import com.arturcapelossi.agilepm.infrastructure.persistence.mapper.SprintMapper;
import org.springframework.stereotype.Component;

import com.arturcapelossi.agilepm.domain.model.enums.SprintStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaSprintRepository implements SprintRepository {

    private final SpringDataSprintRepository jpaRepo;
    private final SprintMapper mapper;

    public JpaSprintRepository(SpringDataSprintRepository jpaRepo, SprintMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public Sprint save(Sprint sprint) {
        var entity = mapper.toEntity(sprint);
        var savedEntity = jpaRepo.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public Optional<Sprint> findById(UUID id) {
        return jpaRepo.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Sprint> findAll() {
        return jpaRepo.findAll().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Sprint> findByProjectIdAndStatus(UUID projectId, SprintStatus status) {
        return jpaRepo.findByProjectEntityIdAndStatus(projectId, status).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        jpaRepo.deleteById(id);
    }
}
