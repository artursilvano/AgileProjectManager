package com.arturcapelossi.agilepm.infrastructure.persistence.repository;

import com.arturcapelossi.agilepm.domain.model.Project;
import com.arturcapelossi.agilepm.domain.repository.ProjectRepository;
import com.arturcapelossi.agilepm.infrastructure.persistence.mapper.ProjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaProjectRepository implements ProjectRepository {

    private final SpringDataProjectRepository jpaRepo;
    private final ProjectMapper mapper;

    public JpaProjectRepository(SpringDataProjectRepository jpaRepo, ProjectMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public Project save(Project project) {
        var entity = mapper.toEntity(project);
        var savedEntity = jpaRepo.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public Optional<Project> findById(UUID id) {
        return jpaRepo.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Project> findAll() {
        return jpaRepo.findAll().stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        jpaRepo.deleteById(id);
    }
}
